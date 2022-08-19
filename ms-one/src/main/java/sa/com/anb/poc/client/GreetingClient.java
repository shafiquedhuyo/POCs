package sa.com.anb.poc.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "${greeting.feign.client.value}", url = "${greeting.feign.client.url}")
public interface GreetingClient {

	@GetMapping(value = "/greeting")
	public String getGreeting();
}
