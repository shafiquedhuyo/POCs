package sa.com.anb.poc.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
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
import sa.com.anb.poc.model.User;
import sa.com.anb.poc.repository.UserRepository;


@RestController
@RequestMapping(value = "/api/users")
@RefreshScope
@Slf4j
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private DiscoveryClient discoveryClient;

	@GetMapping(value = "/service-url")
	public String serviceUrl() {
	    List<ServiceInstance> list = discoveryClient.getInstances("ms-two");
	    if (list != null && !list.isEmpty() ) {
	        return list.get(0).getUri().getRawPath();
	    }
	    return null;
	}
	
	@GetMapping
	public ResponseEntity<List<User>> getAllUsers() {
		try {
			log.info("getAllUsers -- started");
			List<User> users = userRepository.findAll();
			if (users.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			else
				return new ResponseEntity<>(users, HttpStatus.OK);
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
			Optional<User> user = userRepository.findById(Integer.valueOf(id));
			if (user.isPresent())
				return new ResponseEntity<>(user.get(), HttpStatus.OK);
			else
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
			User createdUser = userRepository.save(user);
			return new ResponseEntity<>(createdUser, HttpStatus.CREATED);

		} catch (Exception e) {
			log.error("Error while saving user {}", user, e);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request", e);
		} finally {
			log.info("createUser -- finished");
		}

	}

	@PutMapping(value = "")
	public ResponseEntity<User> updateUser(@RequestBody User user) {
		try {
			log.info("updateUser -- started");
			Optional<User> existingUser = userRepository.findById(user.getId());
			if (existingUser.isPresent()) {
				User userToBeUpdate = existingUser.get();
				userToBeUpdate.setFirstName(user.getFirstName());
				userToBeUpdate.setMiddleName(user.getMiddleName());
				userToBeUpdate.setLastName(user.getLastName());
				userToBeUpdate.setEmail(user.getEmail());
				return new ResponseEntity<>(userRepository.save(userToBeUpdate), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

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
			userRepository.deleteById(Integer.valueOf(id));
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			log.error("Error while getting user with id {}", id, e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong", e);
		} finally {
			log.info("deleteUserById -- finished");
		}

	}

}
