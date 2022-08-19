package com.sa.anb.poc.auditing.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sa.anb.poc.auditing.model.User;
import com.sa.anb.poc.auditing.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping(path = "/add/user")
	public ResponseEntity<User> addUser(@RequestBody User user) {
		log.info("Started- addUser");
		try {
			return new ResponseEntity<>(userService.addUser(user), HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("Error while adding user ", e);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad User request", e);
		} finally {
			log.info("Finished- addUser");
		}
		

	}

	@GetMapping(path = "/get/{userId}")
	public ResponseEntity<User> getUser(@PathVariable(value = "userId") Integer userId) {
		log.info("Started- getUser");
		try {
			Optional<User> user = userService.getUser(userId);
			User returnUser = null;
			if (user.isPresent()) {
				returnUser = user.get();
				return new ResponseEntity<>(returnUser, HttpStatus.OK);
			}else {
				return new ResponseEntity<>(new User(), HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			log.error("Error while getting user ", e);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad User request", e);
		} finally {
			log.info("Finished- getUser");
		}
		
	}
}
