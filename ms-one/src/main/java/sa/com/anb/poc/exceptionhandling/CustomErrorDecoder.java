package sa.com.anb.poc.exceptionhandling;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
	private ErrorDecoder errorDecoder = new Default();

	@Override
	public Exception decode(String methodKey, Response response) {
		ExceptionMessage message = null;
		if (response.body() != null) {
			try (InputStream bodyIs = response.body().asInputStream()) {
				ObjectMapper mapper = new ObjectMapper();
				message = mapper.readValue(bodyIs, ExceptionMessage.class);
			} catch (IOException e) {
				return new Exception(e.getMessage());
			}
		}
		switch (response.status()) {
		case 400:
			return new BadRequestException(
					message != null && message.getMessage() != null ? message.getMessage() : "Bad Request");
		case 404:
			return new NotFoundException(
					message != null && message.getMessage() != null ? message.getMessage() : "Not Found Exception");

		default:
			return errorDecoder.decode(methodKey, response);
		}
	}

}
