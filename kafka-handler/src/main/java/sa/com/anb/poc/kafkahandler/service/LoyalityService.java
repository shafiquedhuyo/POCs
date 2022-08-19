package sa.com.anb.poc.kafkahandler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import sa.com.anb.poc.kafkahandler.exception.ServiceException;
import sa.com.anb.poc.kafkahandler.model.Customer;
import sa.com.anb.poc.kafkahandler.model.User;

@Service
@Slf4j
public class LoyalityService {
	@Autowired
	private WebClient webClient;

	@Value("${service1.api.uri}")
	private String service1ApiUri;

	public User addUser(User usr) throws ServiceException {
		try {
			log.info("Request Body: {} ", new Gson().toJson(usr));
			ResponseEntity<String> response = webClient.post().uri(service1ApiUri)
					.headers(headers->headers.setContentType(MediaType.APPLICATION_JSON))
		            .body(Mono.just(usr), User.class).retrieve()
					.onStatus(status -> status.value() == 401, clientResponse -> Mono.empty()).toEntity(String.class)
					.block();

			if (response != null && response.getStatusCode().is2xxSuccessful())
				return new User();
			else
				return null;
		} catch (WebClientResponseException we) {
			throw new ServiceException(we.getMessage(), we.getRawStatusCode());
		}
	}

	public void updateCustomer(Customer customer, String bearerToken) throws ServiceException {
		try {
			log.info("Request Body: {} ", new Gson().toJson(customer));
			webClient.post().uri(service1ApiUri)
			.headers(headers->{
				headers.setBearerAuth(bearerToken);
				headers.setContentType(MediaType.APPLICATION_JSON);
			})
					.body(Mono.just(customer), Customer.class)
					.exchangeToMono(resp -> {
						if (resp.statusCode().equals(HttpStatus.OK)) {
							return resp.bodyToMono(Customer.class);
						} else {
							return resp.createException().flatMap(Mono::error);
						}
					});
		} catch (WebClientResponseException we) {
			throw new ServiceException(we.getMessage(), we.getRawStatusCode());
		}
	}

}
