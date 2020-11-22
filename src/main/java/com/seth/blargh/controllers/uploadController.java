package com.seth.blargh.controllers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.seth.blargh.entities.Record;
import com.seth.blargh.entities.User;
import com.seth.blargh.repositories.RecordRepository;
import com.seth.blargh.repositories.UserRepository;

@Controller
public class uploadController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(uploadController.class);
	
@Autowired
private RecordRepository recordRepos;



@Autowired
private UserRepository userRepos;

@RequestMapping("/showUpload")
public String showUpload(@RequestParam("userId") int userId, ModelMap modelMap) {
	modelMap.addAttribute("user", userRepos.findById(userId).get());
	return "upload";
	
}

	
@RequestMapping(value="/uploadFile", method =RequestMethod.POST)
public
String uploadFileHandler(@RequestParam("userId") int userId, @RequestParam("file")MultipartFile file, ModelMap modelMap) {
	User user = userRepos.findById(userId).get();
	LOGGER.warn("File name" + file.getName());
	String msg="";
	if (!file.isEmpty()) {
		LOGGER.warn("file is not empty");
		try {
			String[] line;
			
			InputStream is = file.getInputStream();
			LOGGER.warn("Input Stream Made");
			BufferedReader br = new BufferedReader (new InputStreamReader(is));
			LOGGER.warn("BR created");
			CSVReader csvReader = new CSVReader(br);
			line = csvReader.readNext();
			int count=0;
			while((line= csvReader.readNext()) != null) {
				Record record = new Record();
				record.setArtist(line[1]);
			
				record.setTitle(line[2]);
			
				record.setReleaseId(Long.parseLong(line[7]));
				
				record.setUser(user);
				if (recordRepos.checkIfRecordsExist(record.getUser().getId(), record.getArtist(), record.getTitle()).isEmpty()) {
				recordRepos.save(record);
				LOGGER.warn("Record Saved");
				count++;
				}
				LOGGER.warn("Count "+ count);
			}
			csvReader.close();
			msg = "" + count + " records added";

		} catch (Exception e) {
			msg ="You failed to upload " + file.getName() + " => " + e.getMessage();
			
		}
	} else {
		msg= "You failed to upload because the file was empty.";
		
	}
	modelMap.addAttribute("msg", msg);
	modelMap.addAttribute("user", user);
	return "mainpage";
}
}


