package com.seth.blargh.controllers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
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
public class RecordController {

	@Autowired
	UserRepository userRepos;
	@Autowired
	RecordRepository recordRepos;
	@Autowired
	MoodRepository moodRepos;
	@Autowired
	MoodSetterRepository moodSetRepos;

	private static final Logger LOGGER = LoggerFactory.getLogger(RecordController.class);

	@RequestMapping("/recordCreation")
	public String showRecordAdd(@RequestParam("userId") int id, ModelMap modelMap) {
		User user = userRepos.findById(id).get();
		modelMap.addAttribute("user", user);
		List<Mood> moods = moodRepos.findMoods(id);
		modelMap.addAttribute("moods", moods);
		return "addRecord";
	}

	@RequestMapping("/addRecord")
	public String addRecord(@ModelAttribute("record") Record record,
			@RequestParam(value = "moods", required = false) String[] moods, @RequestParam("userId") int id,
			ModelMap modelMap) {
		User user = userRepos.findById(id).get();
		record.setReleaseId(0);
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
		List<Mood> moodss = moodRepos.findMoods(id);
		modelMap.addAttribute("user", user);
		modelMap.addAttribute("moods", moodss);
		return "addRecord";
	}

	@RequestMapping("/showUnassigned")
	public String assignMoods(@RequestParam("userId") int id,
			@RequestParam(value = "mood", required = false) int moodId, ModelMap modelMap) {
		LOGGER.warn("INSIDE Show Unassigned");
		User user = userRepos.findById(id).get();
		if (moodRepos.findById(moodId).isEmpty()) {
			moodId = 0;
		}
		List<Mood> moods = moodRepos.findMoods(id);
		List<Record> records = getRecordsByMoodId(id, moodId);
		modelMap.addAttribute("records", records);
		modelMap.addAttribute("moods", moods);
		modelMap.addAttribute("moodId", moodId);
		modelMap.addAttribute("user", user);
		return "assignMood";

	}

	@RequestMapping(value = "/editRecords")
	public String editRecords(@RequestParam("userId") int userId,
			@RequestParam(value = "mood", required = false) int moodId, ModelMap modelMap) {
		User user = userRepos.findById(userId).get();
		modelMap.addAttribute("user", user);
		if (moodRepos.findById(moodId).isEmpty()) {
			moodId = 0;
		}
		List<Mood> moods = moodRepos.findMoods(userId);
		List<Record> records = getRecordsByMoodId(userId, moodId);
		modelMap.addAttribute("records", records);
		modelMap.addAttribute("moodId", moodId);
		modelMap.addAttribute("moods", moods);

		return "editRecords";

	}

	@RequestMapping(value = "/ShowEditRecord", method = RequestMethod.GET)
	public String showEditRecord(@RequestParam("recordId") long recordId, @RequestParam("userId") int userId,
			@RequestParam("moodId") int moodId, ModelMap modelMap) {
		User user = userRepos.findById(userId).get();
		Record record = recordRepos.findById(recordId);
		List<Mood> moods = new ArrayList<Mood>();
		List<MoodSetter> moodSet = moodSetRepos.findByRecordId(recordId);
		for(MoodSetter ms: moodSet) {
			moods.add(ms.getMood());
		}
		modelMap.addAttribute("record", record);
		modelMap.addAttribute("user", user);
		modelMap.addAttribute("moodId", moodId);
		modelMap.addAttribute("moods",moods);
		return "editRecord";
	}

	@RequestMapping("/DeleteRecord")
	public String deleteRecord(@RequestParam("recordId") long id, @RequestParam("userId") int userId,
			@RequestParam("moodId") int moodId, ModelMap modelMap) {
		User user = userRepos.findById(userId).get();
		Record record = recordRepos.findById(id);
		String msg = "Record " + record.getTitle() + " by " + record.getArtist() + " removed";
		recordRepos.delete(record);
		if (moodRepos.findById(moodId).isEmpty()) {
			moodId = 0;
		}
		List<Record> records = getRecordsByMoodId(userId, moodId);
		modelMap.addAttribute("msg", msg);
		modelMap.addAttribute("user", user);
		modelMap.addAttribute("records", records);
		modelMap.addAttribute("moodId", moodId);
		return "editRecords";
	}

	@RequestMapping("/editRecord")
	public String editRecord(@RequestParam("moodId") int moodId, @RequestParam("artist") String artist,
			@RequestParam("title") String title, @RequestParam("userId") int userId,
			@RequestParam("recordId") long recordId, ModelMap modelMap) {
		User user = userRepos.findById(userId).get();
		Record record = recordRepos.findById(recordId);
		record.setArtist(artist);
		record.setTitle(title);
		if (moodRepos.findById(moodId).isEmpty()) {
			moodId = 0;
		}
		List<Record> records = getRecordsByMoodId(userId, moodId);
		List<Mood> moods = moodRepos.findMoods(userId);
		modelMap.addAttribute("user", user);
		modelMap.addAttribute("records", records);
		modelMap.addAttribute("moodId", moodId);
		modelMap.addAttribute("moods",moods);

		return "editRecords";
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
	
	@RequestMapping("/untagRecord")
	public String untagRecord(@RequestParam("moodId") int moodId, @RequestParam("recordId") long recordId,@RequestParam("userId") int userId,
			ModelMap modelMap) {
		LOGGER.warn("InsideUntagRecord");
		MoodSetter moodSet = moodSetRepos.ifPairExists(recordId, moodId).get(0);
		moodSetRepos.delete(moodSet);
		LOGGER.warn("Moodset removed");
		User user = userRepos.findById(userId).get();
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
	

}
