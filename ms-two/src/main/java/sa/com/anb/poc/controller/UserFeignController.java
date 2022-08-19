package sa.com.anb.poc.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;
import sa.com.anb.poc.client.UserClient;
import sa.com.anb.poc.model.User;
import sa.com.anb.poc.repository.UserRepository;
import sa.com.anb.poc.service.UserService;

@RestController
@RequestMapping(value = "/api/feign/users")
@RefreshScope
@Slf4j
public class UserFeignController {

	@Autowired
	private UserService userService;

	@GetMapping(value = "")
	public ResponseEntity<List<User>> getAllUsers() {
		try {
			log.info("getAllUsers -- started");
			return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error while getting all users", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong", e);
		} finally {
			log.info("getAllUsers -- finished");
		}

	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") String id) {
		try {
			log.info("getUserById -- started");
			return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error while getting user with id {}", id, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong", e);
		} finally {
			log.info("getUserById -- finished");
		}

	}

	@PostMapping(value = "")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		try {
			log.info("createUser -- started");
			return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);

		} catch (Exception e) {
			log.error("Error while saving user {}", user, e);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request", e);
		} finally {
			log.info("createUser -- finished");
		}

	}

	@PutMapping(value = "")
	public ResponseEntity<User> updateUser( @RequestBody User user) {
		try {
			log.info("updateUser -- started");
			return new ResponseEntity<>(userService.updateUser(user), HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error while updating user {}", user, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong", e);
		} finally {
			log.info("updateUser -- finished");
		}

	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<User> deleteUserById(@PathVariable("id") String id) {
		try {
			log.info("deleteUserById -- started");
			userService.deleteUserById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error while getting user with id {}", id, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong", e);
		} finally {
			log.info("deleteUserById -- finished");
		}

	}

}
