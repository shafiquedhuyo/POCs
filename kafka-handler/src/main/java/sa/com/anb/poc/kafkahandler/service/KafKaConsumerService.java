package sa.com.anb.poc.kafkahandler.service;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import lombok.extern.slf4j.Slf4j;
import sa.com.anb.poc.kafkahandler.exception.RestTemplateResponseErrorHandler;
import sa.com.anb.poc.kafkahandler.exception.ServiceException;
import sa.com.anb.poc.kafkahandler.model.User;

@Service
@Slf4j
public class KafKaConsumerService {

	private RestTemplate restTemplate;

	@Value("${service1.api.base.url}")
	private String service1ApiBaseUrl;

	@Value("${service1.api.uri}")
	private String service1ApiUri;
	
	private LoyalityService loyalityService;

	public KafKaConsumerService(RestTemplateBuilder restTemplateBuilder,LoyalityService loyalityService) {
		this.restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
		this.loyalityService = loyalityService;
	}

	@KafkaListener(topics = "${kafka.topic}")
	public void listen(ConsumerRecord<?, ?> consumerRecord, Acknowledgment acknowledgment,
			@Headers MessageHeaders headers) {
		String message = consumerRecord.value().toString();
		StopWatch sw = new StopWatch();
		sw.start();
		log.info(String.format("Message recieved -> %s", message));
		try {
			boolean commitOffsets = handlePayloadRestTemplate(message);
			if (commitOffsets)
				acknowledgment.acknowledge();

		} catch (Exception e) {
			log.error("Error while consuming message : {}", message, e);
		} finally {
			log.info("Message processing time: {} ms", sw.getTime());
		}
	}

	private boolean handlePayloadRestTemplate(String message) {
		JsonObject msgObj = JsonParser.parseString(message).getAsJsonObject();
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("content-type", "application/json; charset=UTF-8");
		HttpEntity<String> requestEntity = new HttpEntity<>(new Gson().toJson(msgObj), requestHeaders);
		ResponseEntity<String> response = restTemplate.exchange(service1ApiBaseUrl + service1ApiUri, HttpMethod.POST,
				requestEntity, String.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			log.info("User created successfully...");
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unused")
	private boolean handlePayloadWebClient(String message) throws JsonSyntaxException, ServiceException {
		User user = loyalityService.addUser(new Gson().fromJson(message, User.class));
		if (user != null) {
			log.info("User added successfully...");
			return true;
		}
		return false;
	}

}