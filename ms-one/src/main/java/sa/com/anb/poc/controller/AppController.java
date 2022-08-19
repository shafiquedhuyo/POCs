package sa.com.anb.poc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import sa.com.anb.poc.client.GreetingClient;

@RestController
@RequestMapping(value = "/api")
@RefreshScope
@Slf4j
public class AppController {

	@Value("${greeting}")
	private String greeting;
	
	@Autowired
	private GreetingClient greetingClient;

	@GetMapping(value = "/greeting")
	public String getGreeting() {
		log.info("Calling getGreeting");
		return greeting;
	}
	
	
	@GetMapping(value = "/feign/greeting")
	public String getFeignGreeting() {
		log.info("Calling getFeignGreeting");
		return greetingClient.getGreeting();
	}
	

}
