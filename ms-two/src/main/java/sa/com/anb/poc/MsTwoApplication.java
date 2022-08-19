package sa.com.anb.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsTwoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsTwoApplication.class, args);
	}

}
