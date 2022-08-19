package sa.com.anb.poc.kafkahandler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.gson.Gson;

import sa.com.anb.poc.kafkahandler.model.User;
import sa.com.anb.poc.kafkahandler.service.KafKaProducerService;

@SpringBootTest
class KafkaHandlerApplicationTests {

	@Autowired
	private KafKaProducerService kafKaProducerService;

	@Test
	void pulishMessage() {
		User user = new User();
		user.setFirstName("Shafique");
		user.setLastName("Rehman");
		user.setMotherName("AA");
		user.setCprNumber("AA1237899");
		Assertions.assertTrue(kafKaProducerService.sendMessage(new Gson().toJson(user)));
	}

}
