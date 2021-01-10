package com.seth.waxandwanerecords.controllers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.opencsv.CSVReader;
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
public class uploadController {

	private static final Logger LOGGER = LoggerFactory.getLogger(uploadController.class);

	@Autowired
	private RecordRepository recordRepos;

	@Autowired
	private GenreRepository genreRepos;

	@Autowired
	private UserRepository userRepos;
	
		

	private User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepos.findByEmail(auth.getName());
		return user;
	}
	
	//shows upload page
	@RequestMapping("/showUpload")
	public String showUpload(ModelMap modelMap) {

		return "upload";

	}
	
	//for uploading the file.  
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public String uploadFileHandler(@RequestParam("file") MultipartFile file,@RequestParam(value = "includeGenres", required=false)Boolean genres,@RequestParam(value = "includeMoods", required=false)Boolean includeMoods, ModelMap modelMap) {
		User user = getUser();
		String key = System.getProperty("Discogs_Key");
		String secret=System.getProperty("Discogs_Secret");
		LOGGER.warn("File name" + file.getName());
		String msg = "";
		if (!file.isEmpty()) {
			LOGGER.warn("file is not empty");
			try {
				String[] line;
				//reads the file and sets the appropriate columns in each row to a record
				InputStream is = file.getInputStream();
				LOGGER.warn("Input Stream Made");
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				LOGGER.warn("BR created");
				CSVReader csvReader = new CSVReader(br);
				line = csvReader.readNext();
				int count = 0;
				while ((line = csvReader.readNext()) != null) {
					Record record = new Record();
					record.setArtist(line[1]);

					record.setTitle(line[2]);

					record.setReleaseId(Long.parseLong(line[7]));

					record.setUser(user);
					
					//makes sure record doesn't already exist
					if (recordRepos.checkIfRecordsExist(record.getUser().getId(), record.getArtist(), record.getTitle())
							.isEmpty()) {
						recordRepos.save(record);
						LOGGER.warn("Record Saved");
						count++;
						//checks if the option to import the genres is checked, if so imports the genres from discogs. Sleep is to make sure it doesn't overload the api.
						if(genres!=null && genres) {
						setGenre(recordRepos.findById(record.getId()),key,secret);
						
						TimeUnit.SECONDS.sleep(1);
						}
					}
				

					LOGGER.warn("Count " + count);
				}
			
				csvReader.close();
				msg = "" + count + " records added";

			} catch (Exception e) {
				msg = "You failed to upload " + file.getName() + " => " + e.getMessage();

			}
		} else {
			msg = "You failed to upload because the file was empty.";

		}
		modelMap.addAttribute("msg", msg);
		modelMap.addAttribute("user", user);
		return "upload";
	}

	
	//takes care of getting the genre from discogs
	public void setGenre(Record record, String key, String secret)
			throws JsonMappingException, JsonProcessingException {
		RestTemplate restTemp = new RestTemplate();
		LOGGER.warn("in the get genre method");
		//gets the information on the release and then selects the genre from the json info.
		ResponseEntity<String> response = restTemp.getForEntity(
				"https://api.discogs.com/releases/" + record.getReleaseId() + "?key=" + key + "&secret=" + secret,
				String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.getBody());
		ArrayNode arrayNode = (ArrayNode) root.get("styles");
		List<String> styles = new ArrayList<String>();
		for (JsonNode j : arrayNode) {
			styles.add(j.asText());
		}
		User user = getUser();
	
		List<Genre> genres = genreRepos.findByRecordId(record.getId());
		//saves genres to db, first checks if it exists before saving
		for (String s : styles) {
			s.replace("\"", "");
			LOGGER.warn(s);
			Genre genre = new Genre();
			genre.setRecord(record);
			genre.setName(s);
			genre.setUser(user);
			boolean exists = false;
			for (Genre g : genres) {
				if (genre.getName().equals(g.getName())) {
					exists = true;

				}

			}
			if (!exists) {
				genreRepos.save(genre);
			}

		}

	}
}
