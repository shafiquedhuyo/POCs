package sa.com.anb.poc.kafkahandler.utils;

import java.nio.charset.StandardCharsets;

import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpField;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import sa.com.anb.poc.kafkahandler.exception.ServiceException;

@Slf4j
public class WebClientFilter {
	private WebClientFilter() {}
	public static ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(request -> {
			logRequestMethodAndUrl(request);
			logRequestHeaders(request);
			
			return logRequestBody(request);
		});
	}

	public static ExchangeFilterFunction logResponse() {
		return ExchangeFilterFunction.ofResponseProcessor(response -> {
			logResponseStatus(response);
			logResponseHeaders(response);

			return logResponseBody(response);
		});
	}

	private static void logResponseStatus(ClientResponse response) {
		HttpStatus status = response.statusCode();
		log.info("Response staus code {} ({})", status.value(), status.getReasonPhrase());
	}
	
	private static Mono<ClientRequest> logRequestBody(ClientRequest resquest) {
				return Mono.just(resquest);
	}

	private static Mono<ClientResponse> logResponseBody(ClientResponse response) {
		if (response.statusCode().is4xxClientError() || response.statusCode().is5xxServerError()) {
			return response.bodyToMono(String.class).flatMap(body -> {
				log.error("Response Body is {}", body);
				return Mono.error(new ServiceException(body, response.rawStatusCode()));
			});
		} else {
			return response.bodyToMono(String.class).flatMap(body -> {
				log.info("Response Body is {}", body);
				return Mono.just(response);
			});
			
		}
	}

	private static void logResponseHeaders(ClientResponse response) {
		log.info("Response Headers");
		response.headers().asHttpHeaders().forEach((name, values) -> {
			values.forEach(value -> {
				logNameAndValuePair(name, value);
			});
		});
	}

	private static void logRequestHeaders(ClientRequest request) {
		log.info("Request Headers");
		request.headers().forEach((name, values) -> {
			values.forEach(value -> {
				logNameAndValuePair(name, value);
			});
		});
	}

	private static void logNameAndValuePair(String name, String value) {
		log.info("{}={}", name, value);
	}

	private static void logRequestMethodAndUrl(ClientRequest request) {
		log.info("Url: {}",request.url());
		log.info("Http Method: {}",request.method().name());
	}
	
	// log request and response in db
	public static Request enhance(Request inboundRequest) {
	    StringBuilder logSB = new StringBuilder();
	    // Request Logging
	    inboundRequest.onRequestBegin(request ->
	    logSB.append("Request: \n")
	            .append("URI: ")
	            .append(request.getURI())
	            .append("\n")
	            .append("Method: ")
	            .append(request.getMethod()));
	    inboundRequest.onRequestHeaders(request -> {
	    	logSB.append("\nHeaders:\n");
	        for (HttpField header : request.getHeaders()) {
	        	logSB.append("\t\t" + header.getName() + " : " + header.getValue() + "\n");
	        }
	    });
	    inboundRequest.onRequestContent((request, content) ->{
	    	
	    	byte[] bytes = new byte[content.remaining()];
	    	content.get(bytes);
	        String newContent = new String(bytes, StandardCharsets.UTF_8);
	    	
	    	logSB.append("Request Body: \n\t")
	    	.append(newContent);
	    	
	    }
	            
	    		
	    		);
	    logSB.append("\n");

	    // Response Logging
	    inboundRequest.onResponseBegin(response ->
	    logSB.append("Response:\n")
	            .append("Status: ")
	            .append(response.getStatus())
	            .append("\n"));
	    inboundRequest.onResponseHeaders(response -> {
	    	logSB.append("Headers:\n");
	       for (HttpField header : response.getHeaders()) {
	    	   logSB.append("\t\t" + header.getName() + " : " + header.getValue() + "\n");
	       }
	    });
	    inboundRequest.onResponseContent(((response, content) -> {
	        var bufferAsString = StandardCharsets.UTF_8.decode(content).toString();
	        logSB.append("Response Body:\n" + bufferAsString);
	    }));

	    // Add actual log invocation
	    log.info("HTTP ->\n");
	    inboundRequest.onRequestSuccess(request -> log.info(logSB.toString()));
	    inboundRequest.onResponseSuccess(response -> log.info(logSB.toString()));

	    // Return original request
	    return inboundRequest;
	}
}