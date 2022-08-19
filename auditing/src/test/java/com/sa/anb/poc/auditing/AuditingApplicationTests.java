package com.sa.anb.poc.auditing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.sa.anb.poc.auditing.model.User;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, classes = AuditingApplication.class)
class AuditingApplicationTests {

	final private static int port = 8080;
	final private static String baseUrl = "http://localhost:";

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void post_addUser_201_created() {
		User user = User.builder().id(1).name("Shafique").email("sbc@gmail.com").build();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<User> request = new HttpEntity<User>(user, headers);
		ResponseEntity<User> response = testRestTemplate.postForEntity(baseUrl + port + "/api/add/user", request,
				User.class);

		Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
		Assertions.assertEquals("Shafique", response.getBody().getName());
	}

}
