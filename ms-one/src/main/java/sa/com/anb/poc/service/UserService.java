package sa.com.anb.poc.service;

import java.util.List;

import sa.com.anb.poc.model.User;

public interface UserService {

	public List<User> getUsers();

	public User getUserById(String id);

	public User createUser(User user);

	public User updateUser(User user);

	public void deleteUserById(String id);

}
