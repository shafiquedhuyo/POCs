package sa.com.anb.poc.ms;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class AppController {

	@PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> createUser(@RequestBody User user) {
		log.info("Create User -- started");
		log.info("Create User request payload : {}", new Gson().toJson(user));
		try {
			return new ResponseEntity<>(user, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("Error while creating user", e);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad User request", e);
		} finally {
			log.info("Create User -- finished");
		}
	}

}
