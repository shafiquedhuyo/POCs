package sa.com.anb.poc.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import sa.com.anb.poc.model.User;

@FeignClient(name = "${user.feign.client.value}")
public interface UserClient {

	@GetMapping(value = "/api/users")
	public List<User> getUsers();

	@GetMapping(value = "/api/users/{id}")
	public User getUserById(@PathVariable("id") String id);
	
	@PostMapping(value = "/api/users")
	public User createUser(User user);
	
	@PutMapping(value = "/api/users")
	public User updateUser(User user);
	
	@DeleteMapping(value = "/api/users/{id}")
	public void deleteUserById(@PathVariable("id") String id);
	
}
