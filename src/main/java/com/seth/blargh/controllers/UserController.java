package com.seth.blargh.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.seth.blargh.entities.User;
import com.seth.blargh.repositories.UserRepository;
import com.seth.blargh.services.SecurityService;

@Controller
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepos;
	
	@Autowired
	private SecurityService securityServ;
	
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String showWelcomePage() {
		System.out.println("Inside of the controller");
		return "index";
	}

	//mapping to show registration page
	@RequestMapping(value="/showReg")
	public String showRegistrationPage() {
		System.out.println("Inside of the controller");
		return "registerUser";
	}
	
	//mapping to show the login page
	@RequestMapping(value="/showLogin")
	public String showLogin() {
		LOGGER.warn("Inside showLogin");
		return "login";
	}
	
	
	//Register user and save them to db
	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public String registerUser(@ModelAttribute("user")User user, ModelMap modelMap) {
		// make sure that the email is not peresent in the db
		if (userRepos.findByEmail(user.getEmail())!=null) {
			modelMap.addAttribute("msg", "Email already in use");
			return "registerUser";
		}
		//encode password and then save
		user.setPassword(encoder.encode(user.getPassword()));
		userRepos.save(user);
		String msg = "User "+ user.getUserName() + " has been registered.";
		modelMap.addAttribute("msg", msg);
		return"login";
	}

	
	//login process
	@RequestMapping(value= "/login",  method =RequestMethod.POST)
	public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, ModelMap modelMap) {
		LOGGER.warn("Inside loginUser");
		//checks match of email and password with db
		boolean loginConfirmation = securityServ.login(email, password);
		LOGGER.warn("login: " +loginConfirmation);
		//find the user in the db
		User user = userRepos.findByEmail(email);
		LOGGER.info("user found: "+ user);
		//successful login flow
		if (loginConfirmation) {
			modelMap.addAttribute("user", user);
			LOGGER.info("Login successfull");
			return"mainpage";
		}
		//login failure flow
		String msg ="Invalid Email and Password Combination";
		modelMap.addAttribute("msg", msg);
		return"login";
	}
	
		//sends to the mainpage with the user object
		@RequestMapping("/mainpage")
		public String returnHome(@RequestParam("userId") int id, ModelMap modelMap) {
		User user = userRepos.findById(id).get();
		modelMap.addAttribute("user", user);
		return"mainpage";
	}
}
