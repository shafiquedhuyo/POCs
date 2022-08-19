package sa.com.anb.poc.kafkahandler.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;
import sa.com.anb.poc.kafkahandler.constants.DownstreamApi;

@Getter
@ToString
public class MyRestTemplateException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DownstreamApi api;
	private HttpStatus statusCode;
	private String error;

	public MyRestTemplateException(DownstreamApi api, HttpStatus statusCode, String error) {
		super(error);
		this.api = api;
		this.statusCode = statusCode;
		this.error = error;
	}

}