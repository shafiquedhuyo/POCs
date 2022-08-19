package com.sa.anb.poc.auditing.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sa.anb.poc.auditing.model.User;
import com.sa.anb.poc.auditing.repository.UserRepository;

@Service
public class UserService {

	private UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User addUser(User user) {
		return userRepository.save(user);
	}

	public Optional<User> getUser(Integer userId) {
		return userRepository.findById(userId);
	}

}
