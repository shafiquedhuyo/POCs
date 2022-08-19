package sa.com.anb.poc.kafkahandler.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String body;
	private int code;
	
	public ServiceException(String body, int code) {
		super();
		this.body = body;
		this.code = code;
	}
	

	
}
