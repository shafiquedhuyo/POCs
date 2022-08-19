package sa.com.anb.poc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sa.com.anb.poc.client.UserClient;
import sa.com.anb.poc.model.User;
import sa.com.anb.poc.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserClient client;

	@Override
	public List<User> getUsers() {
		return client.getUsers();
	}

	@Override
	public User getUserById(String id) {
		return client.getUserById(id);
	}

	@Override
	public User createUser(User user) {
		return client.createUser(user);
	}

	@Override
	public User updateUser(User user) {
		return client.updateUser(user);
	}

	@Override
	public void deleteUserById(String id) {
		client.deleteUserById(id);
		
	}

}
