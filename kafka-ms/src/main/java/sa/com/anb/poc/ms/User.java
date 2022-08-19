package sa.com.anb.poc.ms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
	private String firstName;
	private String lastName;
	private String motherName;
	private String cprNumber;
}
