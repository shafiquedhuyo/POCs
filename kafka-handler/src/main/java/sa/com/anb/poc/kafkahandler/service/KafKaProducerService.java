package sa.com.anb.poc.kafkahandler.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafKaProducerService {

	@Value("${kafka.topic}")
	private String topic;

	private final KafkaTemplate<String, String> kafkaTemplate;

	KafKaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public boolean sendMessage(String message) {
		log.info(String.format("Message sent -> %s", message));
		try {
			this.kafkaTemplate.send(topic, message);
			return true;
		} catch (Exception e) {
			log.error("Error while sending msg to {}  topic", topic, e);
			return false;
		}

	}
}