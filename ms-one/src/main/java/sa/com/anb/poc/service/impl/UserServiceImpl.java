package sa.com.anb.poc.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import sa.com.anb.poc.client.UserClient;
import sa.com.anb.poc.model.User;
import sa.com.anb.poc.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserClient client;

	@Override
	@CircuitBreaker(name = "getUsers",fallbackMethod = "fallbackGetUsers")
	public List<User> getUsers() {
		return client.getUsers();
	}

	@Override
	@CircuitBreaker(name = "getUserById",fallbackMethod = "fallbackGetUserById")
	public User getUserById(String id) {
		return client.getUserById(id);
	}

	@Override
	@CircuitBreaker(name = "createUser",fallbackMethod = "fallbackCreateUser")
	public User createUser(User user) {
		return client.createUser(user);
	}

	@Override
	@CircuitBreaker(name = "updateUser",fallbackMethod = "fallbackUpdateUser")
	public User updateUser(User user) {
		return client.updateUser(user);
	}

	@Override
	@CircuitBreaker(name = "deleteUserById",fallbackMethod = "fallbackDeleteUserById")
	public void deleteUserById(String id) {
		client.deleteUserById(id);
		
	}
	
	public List<User> fallbackGetUsers(Throwable cause){
		return Collections.emptyList();
	}
	
	public User fallbackGetUserById(Throwable cause){
		return null;
	}
	
	public User fallbackCreateUser(Throwable cause){
		return new User();
	}
	
	public User fallbackUpdateUser(Throwable cause){
		return new User();
	}
	
	public void fallbackDeleteUserById(Throwable cause){
		
	}
	

}
