package com.seth.waxandwanerecords.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.seth.waxandwanerecords.entities.Genre;
import com.seth.waxandwanerecords.entities.Mood;
import com.seth.waxandwanerecords.entities.MoodSetter;
import com.seth.waxandwanerecords.entities.Record;
import com.seth.waxandwanerecords.entities.User;
import com.seth.waxandwanerecords.repositories.GenreRepository;
import com.seth.waxandwanerecords.repositories.MoodRepository;
import com.seth.waxandwanerecords.repositories.MoodSetterRepository;
import com.seth.waxandwanerecords.repositories.RecordRepository;
import com.seth.waxandwanerecords.repositories.UserRepository;

@Controller
public class RecordController {

	@Autowired
	UserRepository userRepos;
	@Autowired
	RecordRepository recordRepos;
	@Autowired
	MoodRepository moodRepos;
	@Autowired
	MoodSetterRepository moodSetRepos;
	@Autowired
	GenreRepository genreRepos;
	
	private User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user= userRepos.findByEmail(auth.getName());
		return user;
		}

	private static final Logger LOGGER = LoggerFactory.getLogger(RecordController.class);
	// show jsp
	@RequestMapping("/addRecords")
	public String showRecordPaths() {
		return "addRecords";
	}
	//shows manual record add page
	@RequestMapping("/recordCreation")
	public String showRecordAdd(ModelMap modelMap) {
		User user = getUser();
		modelMap.addAttribute("user", user);
		List<Mood> moods = moodRepos.findMoods(user.getId());
		modelMap.addAttribute("moods", moods);
		return "addRecord";
	}
	
	//adds record to database
	@RequestMapping("/addRecord")
	public String addRecord(@ModelAttribute("record") Record record,
			@RequestParam(value = "moods", required = false) String[] moods,
			ModelMap modelMap) {
		User user = getUser();
		record.setReleaseId(0);
		//goes through string of moods, tagging any checked moods to record added.
		if (moods == null) {
			record.setAssigned(false);
			record.setUser(user);
			recordRepos.save(record);
		} else {
			record.setAssigned(true);
			record.setUser(user);
			recordRepos.save(record);

			for (String m : moods) {
				MoodSetter moodSet = new MoodSetter();
				Mood x = moodRepos.findById(Integer.parseInt(m)).get();
				moodSet.setMood(x);
				moodSet.setRecord(record);
				moodSet.setUser(user);
				moodSetRepos.save(moodSet);
			}
		}
		//returns the list of moods to show as options again
		List<Mood> moodss = moodRepos.findMoods(user.getId());
		modelMap.addAttribute("user", user);
		modelMap.addAttribute("moods", moodss);
		return "addRecord";
	}
	
	
	//brings up the records not assigned to the mood selected to allow tagging to mood
	@RequestMapping("/showUnassigned")
	public String assignMoods(
			@RequestParam(value = "mood") int moodId, ModelMap modelMap) {
		LOGGER.warn("INSIDE Show Unassigned");
		User user = getUser();
		//in case no mood is selected to prevent an error
		if (moodRepos.findById(moodId).isEmpty()) {
			moodId = 0;
		}
		
		//gets all moods and all records for the user
		List<Mood> moods = moodRepos.findMoods(user.getId());
		List<Record> records = recordRepos.findRecord(user.getId());
		//removes records tagged with that id
		records.removeAll(
		 getRecordsByMoodId(user.getId(), moodId));
		//Gets the genres assigned to the user, removes duplicates.  Genre database was setup late at night and was not the best implementation. 
		List<Genre> genres = genreRepos.check(user.getId());
		if (!genreRepos.check(user.getId()).isEmpty()) {
		Collection <String> genreDup = new HashSet<String>();
		for (Genre g: genres) {
			genreDup.add(g.getName());
		}
			modelMap.addAttribute("genres", genreDup);
		}
		if (moodId !=0) {
		modelMap.addAttribute("mood", moodRepos.findById(moodId).get());
		}
		modelMap.addAttribute("records", records);
		modelMap.addAttribute("moods", moods);
		modelMap.addAttribute("moodId", moodId);
		modelMap.addAttribute("user", user);
		return "assignMood";

	}
	
	//shows the edit records page and sends all needed data
	@RequestMapping(value = "/editRecords")
	public String editRecords(
			@RequestParam(value = "mood", required = false) int moodId, ModelMap modelMap) {
		User user = getUser();
		modelMap.addAttribute("user", user);
		if (moodRepos.findById(moodId).isEmpty()) {
			moodId = 0;
		}
		List<Mood> moods = moodRepos.findMoods(user.getId());
		List<Record> records = getRecordsByMoodId(user.getId(), moodId);
		modelMap.addAttribute("records", records);
		modelMap.addAttribute("moodId", moodId);
		modelMap.addAttribute("moods", moods);
		return "editRecords";
	}
	
	
	//Sends information to the page for editing a record.  Send the Moods assigned to the record along with whether moods assigned is false. 
	@RequestMapping(value = "/ShowEditRecord", method = RequestMethod.GET)
	public String showEditRecord(@RequestParam("recordId") long recordId,
			@RequestParam("moodId") int moodId, ModelMap modelMap) {
		User user = getUser();
		Record record = recordRepos.findById(recordId);
		List<Mood> moods = new ArrayList<Mood>();
		List<MoodSetter> moodSet = moodSetRepos.findByRecordId(recordId);
		for(MoodSetter ms: moodSet) {
			moods.add(ms.getMood());
		}
		Boolean emptyMoods = false;
		if (moods.isEmpty()) {
			emptyMoods=true;
		}
		modelMap.addAttribute("emptyMoods", emptyMoods);
		modelMap.addAttribute("record", record);
		modelMap.addAttribute("user", user);
		modelMap.addAttribute("moodId", moodId);
		modelMap.addAttribute("moods",moods);
		return "editRecord";
	}
	
	
	//removes the record from the database. Database is set to cascade so mood assignments will also be removed. 
	@RequestMapping("/DeleteRecord")
	public String deleteRecord(@RequestParam("recordId") long id,
			@RequestParam("moodId") int moodId, ModelMap modelMap) {
		User user = getUser();
		Record record = recordRepos.findById(id);
		String msg = "Record " + record.getTitle() + " by " + record.getArtist() + " removed";
		recordRepos.delete(record);
		if (moodRepos.findById(moodId).isEmpty()) {
			moodId = 0;
		}
		List<Record> records = getRecordsByMoodId(user.getId(), moodId);
		List<Mood> moods = moodRepos.findMoods(user.getId());
		modelMap.addAttribute("moods",moods);
		modelMap.addAttribute("msg", msg);
		modelMap.addAttribute("user", user);
		modelMap.addAttribute("records", records);
		modelMap.addAttribute("moodId", moodId);
		return "editRecords";
	}
	
	//updates the record with the new information. 
	@RequestMapping("/editRecord")
	public String editRecord(@RequestParam("moodId") int moodId, @RequestParam("artist") String artist,
			@RequestParam("title") String title,
			@RequestParam("recordId") long recordId, ModelMap modelMap) {
		User user = getUser();
		Record record = recordRepos.findById(recordId);
		record.setArtist(artist);
		record.setTitle(title);
		if (moodRepos.findById(moodId).isEmpty()) {
			moodId = 0;
		}
		List<Record> records = getRecordsByMoodId(user.getId(), moodId);
		List<Mood> moods = moodRepos.findMoods(user.getId());
		modelMap.addAttribute("user", user);
		modelMap.addAttribute("records", records);
		modelMap.addAttribute("moodId", moodId);
		modelMap.addAttribute("moods",moods);

		return "editRecords";
	}
	
	
	// returns a list of records based on the mood id. 
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
	
	
	//allows for record to be untagged
	@RequestMapping("/untagRecord")
	public String untagRecord(@RequestParam("moodId") int moodId, @RequestParam("recordId") long recordId,
			ModelMap modelMap) {
		MoodSetter moodSet = moodSetRepos.ifPairExists(recordId, moodId).get(0);
		moodSetRepos.delete(moodSet);
		LOGGER.warn("Moodset removed");
		User user = getUser();
		Record record = recordRepos.findById(recordId);
		List<Mood> moods = new ArrayList<Mood>();
		List<MoodSetter> moodSetter = moodSetRepos.findByRecordId(recordId);
		for(MoodSetter ms: moodSetter) {
			moods.add(ms.getMood());
		}
		modelMap.addAttribute("record", record);
		modelMap.addAttribute("user", user);
		modelMap.addAttribute("moodId", moodId);
		modelMap.addAttribute("moods",moods);
		return "editRecord";
	}
	
	
	//search function for the records. 
	@RequestMapping("/filterRecords")
	public String filterRecords(@RequestParam("query") String query,@RequestParam("moodId") int moodId, @RequestParam("string") String path, ModelMap modelMap) {
		User user = getUser();
		List<Record> recordsReturn = recordRepos.findRecord(user.getId());
		if (moodId!=0) {
		recordsReturn.removeAll(
		 getRecordsByMoodId(user.getId(), moodId));
		}
		List<Record> records = new ArrayList<Record>();
		for(Record record: recordsReturn) {
			if(record.getArtist().toLowerCase().contains(query.toLowerCase()) || record.getTitle().toLowerCase().contains(query.toLowerCase())) {
				records.add(record);
			}
		}
		
		List<Genre> genres = genreRepos.check(user.getId());
		if (!genreRepos.check(user.getId()).isEmpty()) {
		Collection <String> genreDup = new HashSet<String>();
		for (Genre g: genres) {
			genreDup.add(g.getName());
		}
			modelMap.addAttribute("genres", genreDup);
		
		}
		List<Mood> moods = moodRepos.findMoods(user.getId());
		modelMap.addAttribute("records", records);
		modelMap.addAttribute("moodId", moodId);
		modelMap.addAttribute("moods", moods);
		return path;
	}
	
	//returns records from selected genre not tagged to selected mood. 
	@RequestMapping("/filterByGenre")
	public String filterByGenre(@RequestParam("genre")String genreName,@RequestParam("moodId") int moodId, ModelMap modelMap) {
		User user = getUser();
		List<Record> records = recordRepos.findRecord(user.getId());
		if(moodId!=0) {
			records.removeAll(getRecordsByMoodId(user.getId(),moodId));
			
		}
		List<Genre>genre = genreRepos.getByName(user.getId(),genreName);
		List<Record> genreRecords = new ArrayList<Record>();
		//removes records not in selected genre
		for (Genre g : genre) {
			genreRecords.add(g.getRecord());
		}
		records.retainAll(genreRecords);
		
		List<Mood> moods = moodRepos.findMoods(user.getId());
		
		List<Genre> genres = genreRepos.check(user.getId());
		if (!genreRepos.check(user.getId()).isEmpty()) {
		Collection <String> genreDup = new HashSet<String>();
		for (Genre g: genres) {
			genreDup.add(g.getName());
		}
			modelMap.addAttribute("genres", genreDup);
		
		}
		modelMap.addAttribute("currentGenre", genreName);
		modelMap.addAttribute("mood", moodRepos.findById(moodId).get());
		modelMap.addAttribute("moods", moods);
		modelMap.addAttribute("records", records);
		modelMap.addAttribute("moodId", moodId);
		modelMap.addAttribute("user", user);
		return "assignMood";
	}
		
		
		
	
	

}
