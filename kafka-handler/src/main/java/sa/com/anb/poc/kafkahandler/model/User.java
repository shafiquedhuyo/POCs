package sa.com.anb.poc.kafkahandler.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
	private String firstName;
	private String lastName;
	private String motherName;
	private String cprNumber;
}
