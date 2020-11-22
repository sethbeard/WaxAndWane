package com.seth.blargh.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.seth.blargh.entities.Mood;
import com.seth.blargh.entities.MoodSetter;
import com.seth.blargh.entities.Record;
import com.seth.blargh.entities.User;
import com.seth.blargh.repositories.MoodRepository;
import com.seth.blargh.repositories.MoodSetterRepository;
import com.seth.blargh.repositories.RecordRepository;
import com.seth.blargh.repositories.UserRepository;

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
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MoodSetterController.class);
	
	@RequestMapping(value = "/assigningValues", method = RequestMethod.POST)
	public String saveAssignments(@RequestParam(value = "moods", required = false) String[] moods,
			@RequestParam(value = "records") long recordId, @RequestParam("userId") int userId, @RequestParam("moodId") int moodId, ModelMap modelMap) {
		LOGGER.warn("INSIDE SAVE ASSIGNMENTS");

		Record record = recordRepos.findById(recordId);
		User user = userRepos.findById(userId).get();
		LOGGER.warn("Moods " + moods + " Record" + recordId);

		if (moods != null) {
			record.setAssigned(true);
		}
		record.setUser(user);
		recordRepos.save(record);

		for (String m : moods) {
		
			
			MoodSetter moodSet = new MoodSetter();
			Mood x = moodRepos.findById(Integer.parseInt(m)).get();
			
			List<MoodSetter> moodset = moodSetRepos.ifPairExists(record.getId(),x.getId());
			boolean exists = false;
			for(MoodSetter ms : moodset) {
				if (ms.getRecord().getId()==record.getId()) {
					exists=true;
				}
				
			}
			if (!exists) {
				LOGGER.warn("Moodset is empty");
			moodSet.setMood(x);
			moodSet.setRecord(record);
			moodSet.setUser(user);
			moodSetRepos.save(moodSet);
			}
			
		}
		if (moodRepos.findById(moodId).isEmpty()) {
			moodId = 0;
		}
		List<Mood> moodsReturn = moodRepos.findMoods(userId);
		List<Record> records = getRecordsByMoodId(userId, moodId);
		modelMap.addAttribute("records", records);
		modelMap.addAttribute("moods", moodsReturn);
		modelMap.addAttribute("moodId", moodId);
		modelMap.addAttribute("user", user);
		return "assignMood";
	}

	@RequestMapping("/moodPicked")
	public String findRandomRecord(@RequestParam("moodId") int moodId, @RequestParam("userId") int userId,
			ModelMap modelMap) {
		User user = userRepos.findById(userId).get();
		Mood mood = moodRepos.findById(moodId).get();
		int result=0;
		List<MoodSetter> moodset = moodSetRepos.findRecord(userId, moodId);
		if(!moodset.isEmpty()) {
		Random r = new Random();
		result = r.nextInt(moodset.size());
		modelMap.addAttribute("record", moodset.get(result).getRecord());
		}
		else {
			Record record = new Record();
			record.setArtist("Whomp Whomp");
			record.setTitle("No Records Tagged with this one.");
			modelMap.addAttribute("record", record);
		}
		List<Mood> moods = moodRepos.findMoods(userId);
		
		modelMap.addAttribute("user", user);
		modelMap.addAttribute("moods", moods);
		modelMap.addAttribute("mood", mood);
		return "listenToThis";
	}
	
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
	
	@RequestMapping("/UntagRecord")
	public String untagRecord(@RequestParam("moodId") int moodId, @RequestParam("recordId") long recordId,@RequestParam("userId") int userId,
			ModelMap modelMap) {
		LOGGER.warn("InsideUntagRecord");
		User user = userRepos.findById(userId).get();
		MoodSetter moodSet = moodSetRepos.ifPairExists(recordId, moodId).get(0);
		moodSetRepos.delete(moodSet);
		LOGGER.warn("Moodset removed");
		modelMap.addAttribute("records", getRecordsByMoodId(userId,moodId));
		modelMap.addAttribute("moods", moodRepos.findMoods(userId));
		modelMap.addAttribute("moodId", moodId);
		modelMap.addAttribute("user", user);
		
		return "assignMood";
	}
	

}
