package com.seth.waxandwanerecords.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seth.waxandwanerecords.entities.Mood;
import com.seth.waxandwanerecords.entities.MoodSetter;
import com.seth.waxandwanerecords.entities.Record;
import com.seth.waxandwanerecords.entities.User;
import com.seth.waxandwanerecords.repositories.MoodRepository;
import com.seth.waxandwanerecords.repositories.MoodSetterRepository;
import com.seth.waxandwanerecords.repositories.RecordRepository;
import com.seth.waxandwanerecords.repositories.UserRepository;

@Controller
public class MoodSetterController {
	
	@Autowired
	UserRepository userRepos;
	@Autowired
	RecordRepository recordRepos;
	@Autowired
	MoodRepository moodRepos;
	@Autowired
	MoodSetterRepository moodSetRepos;
	
	//gets user from auth to use in controller
	private User getUser() {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	User user= userRepos.findByEmail(auth.getName());
	return user;
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MoodSetterController.class);
	
	
	//assigns values to moods, receives a string of record ids checked along with the mood currently being worked in
	@RequestMapping(value = "/assigningValues", method = RequestMethod.POST)
	public String saveAssignments(@RequestParam(value = "checkedRecord", required = false) String[] records,
			@RequestParam(value = "moodId") int moodId, ModelMap modelMap) {
		LOGGER.warn("INSIDE SAVE ASSIGNMENTS");
		User user = getUser();
		Mood mood = moodRepos.findById(moodId).get();
		List<Mood> moods = moodRepos.findMoods(user.getId());
		//makes sure records were selected
		if (records != null) {
		
		//pulls up each record by the id
		for (String r : records) {
			MoodSetter moodSet = new MoodSetter();
			Record record = recordRepos.findById(Integer.parseInt(r));
			
			//finds all of the moods assigned for the record
			List<MoodSetter> moodset = moodSetRepos.findByRecordId(record.getId());
			Boolean exists = false;
			//makes sure that the current assignement doesn't already exist
			for(MoodSetter m: moodset) {
				if (m.getMood().getId()==mood.getId()) {
					exists=true;
				}
			}
			//creates a new moodset if it doesn't already exist
			if (!exists) {
			LOGGER.warn("Moodset is empty");
			moodSet.setMood(mood);
			moodSet.setRecord(record);
			moodSet.setUser(user);
			moodSetRepos.save(moodSet);
			}
			
		}
		}
		if (moodRepos.findById(moodId).isEmpty()) {
			moodId = 0;
		}
		//retrieves all records for user and then removes ones already tagged with the mood
		List<Record> recordsReturn = recordRepos.findRecord(user.getId());
		recordsReturn.removeAll(
		 getRecordsByMoodId(user.getId(), moodId));
		modelMap.addAttribute("records", recordsReturn);
		modelMap.addAttribute("moodId", moodId);
		modelMap.addAttribute("moods", moods);
		modelMap.addAttribute("user", user);
		return "assignMood";
	}
	
	//returns a random record for the user based on mood picked
	@RequestMapping("/moodPicked")
	public String findRandomRecord(@RequestParam("moodId") int moodId,
			ModelMap modelMap) throws JsonMappingException, JsonProcessingException {
		User user = getUser();
		String key = System.getProperty("Discogs_Key");
		String secret=System.getProperty("Discogs_Secret");
		Mood mood = moodRepos.findById(moodId).get();
		List<Integer> results = new LinkedList<Integer>();
		//finds all records for user tagged with that mood
		List<MoodSetter> moodset = moodSetRepos.findRecord(user.getId(), moodId);
		//makes sure there are results and then creates a list of random records, chooses random item from list (just to increase randomness chance)
		if(!moodset.isEmpty()) {
		Random r = new Random();
		for(int i=0;i<50;i++) {
		results.add( r.nextInt(moodset.size()));
		}
		int result = results.get(r.nextInt(results.size()));
		Record record = moodset.get(result).getRecord();
		modelMap.addAttribute("record",record );
		
		//makes sure there's a discogs id available
		if (record.getReleaseId()!=0) {
		//gets the image for the release from discogs
		RestTemplate restTemp = new RestTemplate();
		ResponseEntity<String> response = restTemp.getForEntity("https://api.discogs.com/releases/"+ record.getReleaseId() +"?key="+key +"&secret="+secret, String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.getBody());
		JsonNode images = root.at("/images");
		String imageUrl= ""+ images.get(0).at("/uri").asText();
		modelMap.addAttribute("imageUrl", imageUrl);
		}
		}
		//returns message if no records found
		else {
			Record record = new Record();
			record.setArtist("Whomp Whomp");
			record.setTitle("No Records Tagged with this one.");
			modelMap.addAttribute("record", record);
		}
		List<Mood> moods = moodRepos.findMoods(user.getId());
		modelMap.addAttribute("user", user);
		modelMap.addAttribute("moods", moods);
		modelMap.addAttribute("mood", mood);
		return "listenToThis";
	}
	
	//returns a list of all records per mood id. 
	private List<Record> getRecordsByMoodId(int userId, int moodId) {

		if (moodId == 0) {

			List<Record> records = recordRepos.findRecord(userId);
			return records;

		} else {
			List<MoodSetter> moodSet = moodSetRepos.findRecord(userId, moodId);
			List<Record> records = new LinkedList<Record>();
			for (MoodSetter moodSetter : moodSet) {
				records.add(moodSetter.getRecord());
			}
			return records;

		}

	}
	
	//removes moodset for specified record & mood
	@RequestMapping("/UntagRecord")
	public String untagRecord(@RequestParam("moodId") int moodId, @RequestParam("recordId") long recordId,
			ModelMap modelMap) {
		LOGGER.warn("InsideUntagRecord");
		User user = getUser();
		MoodSetter moodSet = moodSetRepos.ifPairExists(recordId, moodId).get(0);
		moodSetRepos.delete(moodSet);
		LOGGER.warn("Moodset removed");
		modelMap.addAttribute("records", getRecordsByMoodId(user.getId(),moodId));
		modelMap.addAttribute("moods", moodRepos.findMoods(user.getId()));
		modelMap.addAttribute("moodId", moodId);
		modelMap.addAttribute("user", user);
		
		return "assignMood";
	}
	

}
