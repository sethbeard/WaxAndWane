package com.seth.waxandwanerecords.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.seth.waxandwanerecords.entities.User;
import com.seth.waxandwanerecords.repositories.UserRepository;
import com.seth.waxandwanerecords.services.SecurityService;

@Controller
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepos;

	@Autowired
	private SecurityService securityServ;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String showWelcomePage(ModelMap modelMap) {
		modelMap.addAttribute("demoEmail", "waxandwanerecords@gmail.com");
		modelMap.addAttribute("demoPassword","DemoAccountLogin");
		return "index";
	}

	// mapping to show registration page
	@RequestMapping(value = "/showReg")
	public String showRegistrationPage() {
		System.out.println("Inside of the controller");
		return "registerUser";
	}

	// mapping to show the login page
	@RequestMapping(value = "/showLogin")
	public String showLogin() {
		LOGGER.warn("Inside showLogin");
		return "login";
	}

	// Register user and save them to db
	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public String registerUser(@ModelAttribute("user") User user, ModelMap modelMap) {
		// make sure that the email is not present in the db
		if (userRepos.findByEmail(user.getEmail()) != null) {
			modelMap.addAttribute("msg", "Email already in use");
			return "registerUser";
		}
		// encode password and then save
		user.setPassword(encoder.encode(user.getPassword()));
		userRepos.save(user);
		String msg = "User " + user.getUserName() + " has been registered.";
		modelMap.addAttribute("msg", msg);
		return "login";
	}

	// login process
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password,
			ModelMap modelMap) {
		LOGGER.warn("Inside loginUser");
		// checks match of email and password with db
		boolean loginConfirmation = securityServ.login(email, password);
		LOGGER.warn("login: " + loginConfirmation);
		
		// successful login flow
		if (loginConfirmation) {
			// find the user in the db
			User user = userRepos.findByEmail(email);
			LOGGER.info("user found: " + user);
			modelMap.addAttribute("user", user);
			LOGGER.info("Login successfull");
			return "mainpage";
		}
		// login failure flow
		String msg = "Invalid Email and Password Combination";
		modelMap.addAttribute("msg", msg);
		return "login";
	}

	// sends to the mainpage with the user object
	@RequestMapping("/mainpage")
	public String returnHome( ModelMap modelMap) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user= userRepos.findByEmail(auth.getName());
		
		modelMap.addAttribute("user", user);
		return "mainpage";
	}

	@RequestMapping("/showAbout")
	public String showAbout( ModelMap modelMap) {

		return "About";
	}



}
