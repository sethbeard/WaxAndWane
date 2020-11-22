package com.seth.blargh.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.seth.blargh.entities.Mood;
import com.seth.blargh.entities.User;
import com.seth.blargh.repositories.MoodRepository;
import com.seth.blargh.repositories.MoodSetterRepository;
import com.seth.blargh.repositories.UserRepository;

@Controller
public class MoodController {

	@Autowired
	MoodRepository moodRepos;
	@Autowired
	UserRepository userRepos;
	@Autowired
	MoodSetterRepository moodSetRepos;

//sends them to the page to create and edit moods
	@RequestMapping(value = "/moodCreation", method = RequestMethod.POST)
	public String showAddMood(@RequestParam("user") int id, ModelMap modelMap) {
		// find user
		User user = userRepos.findById(id).get();
		// create a list of all moods for user id
		List<Mood> moods = moodRepos.findMoods(id);
		modelMap.addAttribute("moods", moods);
		modelMap.addAttribute(user);
		return "AddMood";
	}

//shows the edit mood option.  Sends the mood and user back to the jsp
	@RequestMapping(value = "/ShowEditMood", method = RequestMethod.GET)
	public String showEditMood(@RequestParam("moodId") int id, @RequestParam("userId") int userId, ModelMap modelMap) {
		User user = userRepos.findById(userId).get();
		Mood mood = moodRepos.findById(id).get();
		modelMap.addAttribute("mood", mood);
		modelMap.addAttribute("user", user);
		return "editMood";
	}

//adds the mood to the db if it doesn't already exist for the user. 
	@RequestMapping(value = "/addMood", method = RequestMethod.POST)
	public String addMood(@ModelAttribute("mood") Mood mood, @RequestParam("userId") int id, ModelMap modelMap) {
		String msg;
		User user = userRepos.findById(id).get();
		// Check to see if mood exists 
		if (moodRepos.findMoodByName(id, mood.getName()) != null) {
			msg = "Mood already exists";
		//Check if mood is empty
		} else if (mood.getName().equals("")) {
			msg = "Please Enter A Name";
		} 
		//save user 
		else {
			mood.setUser(user);
			moodRepos.save(mood);
			msg = "Mood " + mood.getName() + " created";
		}
		List<Mood> moods = moodRepos.findMoods(user.getId());
		modelMap.addAttribute("msg", msg);
		modelMap.addAttribute("user", user);
		modelMap.addAttribute("moods", moods);
		return "AddMood";
	}

	// Deletes mood
	@RequestMapping("/DeleteMood")
	public String deleteMood(@RequestParam("moodId") int id, @RequestParam("userId") int userId, ModelMap modelMap) {
		User user = userRepos.findById(userId).get();
		String msg = "Mood " + moodRepos.findById(id).get().getName() + " removed";
		moodRepos.deleteById(id);
		List<Mood> moods = moodRepos.findMoods(user.getId());
		modelMap.addAttribute("msg", msg);
		modelMap.addAttribute("user", user);
		modelMap.addAttribute("moods", moods);
		return "AddMood";
	}

	//updates the mood
	@RequestMapping("/editmood")
	public String editMood(@RequestParam("moodId") int id, @RequestParam("name") String name,
			@RequestParam("userId") int userId, ModelMap modelMap) {
		User user = userRepos.findById(userId).get();
		Mood mood = moodRepos.findById(id).get();
		mood.setName(name);
		List<Mood> moods = moodRepos.findMoods(user.getId());
		moodRepos.save(mood);
		modelMap.addAttribute("user", user);
		modelMap.addAttribute("moods", moods);
		return "AddMood";
	}

	
	//returns list of moods
	@RequestMapping("/pickAMood")
	public String showMoodPick(@RequestParam("userId") int id, ModelMap modelMap) {
		User user = userRepos.findById(id).get();
		List<Mood> moods = moodRepos.findMoods(id);
		modelMap.addAttribute("moods", moods);
		modelMap.addAttribute(user);
		return "PickMood";
	}

}