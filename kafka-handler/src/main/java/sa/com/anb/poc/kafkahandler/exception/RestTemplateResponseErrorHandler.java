package sa.com.anb.poc.kafkahandler.exception;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

import sa.com.anb.poc.kafkahandler.constants.DownstreamApi;

@Component
public class RestTemplateResponseErrorHandler extends DefaultResponseErrorHandler {

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError() ) {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()))) {
				String httpBodyResponse = reader.lines().collect(Collectors.joining(""));
				String errorMessage = httpBodyResponse;

				throw new MyRestTemplateException(DownstreamApi.API_1, response.getStatusCode(), errorMessage);
			}
		}
	}
}