package sa.com.anb.poc.kafkahandler;

import java.net.URI;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.JettyClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.handler.logging.LogLevel;
import reactor.netty.transport.logging.AdvancedByteBufFormat;
import sa.com.anb.poc.kafkahandler.utils.WebClientFilter;

@SpringBootApplication
public class KafkaHandlerApplication {

	@Value("${service1.api.base.url}")
	private String service1ApiBaseUrl;

	public static void main(String[] args) {
		SpringApplication.run(KafkaHandlerApplication.class, args);
	}

	@Bean
	public WebClient getWebClient() {
		return WebClient.builder()

				.baseUrl(service1ApiBaseUrl).filter(WebClientFilter.logRequest()).filter(WebClientFilter.logResponse())
				.build();
	}

	@Bean
	public JettyClientHttpConnector getJettyClientHttpConnector() {
		SslContextFactory.Client sslContextFactory = new SslContextFactory.Client();
		HttpClient httpClient = new HttpClient(sslContextFactory) {
			@Override
			public Request newRequest(URI uri) {
				Request request = super.newRequest(uri);
				return WebClientFilter.enhance(request);
			}
		};

		return new JettyClientHttpConnector(httpClient);
	}

	@Bean
	public ReactorClientHttpConnector getReactorClientHttpConnector() {

		reactor.netty.http.client.HttpClient httpClient = reactor.netty.http.client.HttpClient.create()
				.wiretap("reactor.netty.http.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);
		return new ReactorClientHttpConnector(httpClient);
	}
}
