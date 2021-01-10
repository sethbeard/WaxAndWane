package com.seth.waxandwanerecords.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.seth.waxandwanerecords.entities.Mood;
import com.seth.waxandwanerecords.entities.User;
import com.seth.waxandwanerecords.repositories.MoodRepository;
import com.seth.waxandwanerecords.repositories.MoodSetterRepository;
import com.seth.waxandwanerecords.repositories.UserRepository;

@Controller
public class MoodController {

	@Autowired
	MoodRepository moodRepos;
	@Autowired
	UserRepository userRepos;
	@Autowired
	MoodSetterRepository moodSetRepos;

	// gets the user from the authentication to use within the controller
	private User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepos.findByEmail(auth.getName());
		return user;
	}

	// sends them to the page to create and edit moods
	@RequestMapping(value = "/moodCreation", method = RequestMethod.POST)
	public String showAddMood(ModelMap modelMap) {
		User user = getUser();
		// create a list of all moods for user id
		List<Mood> moods = moodRepos.findMoods(user.getId());
		modelMap.addAttribute("moods", moods);
		modelMap.addAttribute(user);
		return "AddMood";
	}

	//shows the edit mood option.  Sends the mood and user back to the jsp
	@RequestMapping(value = "/ShowEditMood", method = RequestMethod.GET)
	public String showEditMood(@RequestParam("moodId") int id, ModelMap modelMap) {
		User user = getUser();
		Mood mood = moodRepos.findById(id).get();
		modelMap.addAttribute("mood", mood);
		modelMap.addAttribute("user", user);
		return "editMood";
	}

	//adds the mood to the database if it doesn't already exist for the user. 
	@RequestMapping(value = "/addMood", method = RequestMethod.POST)
	public String addMood(@ModelAttribute("mood") Mood mood, ModelMap modelMap) {
		String msg;
		User user = getUser();
		// Check to see if mood exists
		if (moodRepos.findMoodByName(user.getId(), mood.getName()) != null) {
			msg = "Mood already exists";
			// Check if mood is empty
		} else if (mood.getName().equals("")) {
			msg = "Please Enter A Name";
		}
		// save user
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
	public String deleteMood(@RequestParam("moodId") int id, ModelMap modelMap) {
		String msg = "Mood " + moodRepos.findById(id).get().getName() + " removed";
		User user = getUser();
		moodRepos.deleteById(id);
		List<Mood> moods = moodRepos.findMoods(user.getId());
		modelMap.addAttribute("msg", msg);
		modelMap.addAttribute("user", user);
		modelMap.addAttribute("moods", moods);
		return "AddMood";
	}

	// updates the mood
	@RequestMapping("/editmood")
	public String editMood(@RequestParam("moodId") int id, @RequestParam("name") String name, ModelMap modelMap) {
		User user = getUser();
		Mood mood = moodRepos.findById(id).get();
		mood.setName(name);
		List<Mood> moods = moodRepos.findMoods(user.getId());
		moodRepos.save(mood);
		modelMap.addAttribute("user", user);
		modelMap.addAttribute("moods", moods);
		return "AddMood";
	}

	// returns list of moods
	@RequestMapping("/pickAMood")
	public String showMoodPick(ModelMap modelMap) {
		User user = getUser();
		List<Mood> moods = moodRepos.findMoods(user.getId());
		modelMap.addAttribute("moods", moods);
		modelMap.addAttribute(user);
		return "PickMood";
	}

}