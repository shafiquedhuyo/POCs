package sa.com.anb.poc.kafkahandler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sa.com.anb.poc.kafkahandler.service.KafKaProducerService;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaProducerController
{
	private final KafKaProducerService producerService;

	@Autowired
	public KafkaProducerController(KafKaProducerService producerService)
	{
		this.producerService = producerService;
	}

	@GetMapping(value = "/publish")
	public void sendMessageToKafkaTopic(@RequestParam("message") String message)
	{
		this.producerService.sendMessage(message);
	}
}