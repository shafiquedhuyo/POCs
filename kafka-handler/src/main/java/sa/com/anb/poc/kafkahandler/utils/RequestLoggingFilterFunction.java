package sa.com.anb.poc.kafkahandler.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.client.reactive.ClientHttpRequestDecorator;
import org.springframework.lang.NonNull;
import org.springframework.util.StopWatch;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Math.min;
import static java.util.UUID.randomUUID;

@Slf4j
@RequiredArgsConstructor
public class RequestLoggingFilterFunction implements ExchangeFilterFunction {

	private static final int MAX_BYTES_LOGGED = 4_096;

	private final String externalSystem;

	@Override
	@NonNull
	public Mono<ClientResponse> filter(@NonNull ClientRequest request, @NonNull ExchangeFunction next) {
		if (!log.isDebugEnabled()) {
			return next.exchange(request);
		}

		var clientRequestId = randomUUID().toString();

		var requestLogged = new AtomicBoolean(false);
		var responseLogged = new AtomicBoolean(false);

		var capturedRequestBody = new StringBuilder();
		var capturedResponseBody = new StringBuilder();

		var stopWatch = new StopWatch();
		stopWatch.start();

		return next.exchange(ClientRequest.from(request).body(new BodyInserter<>() {

			@Override
			@NonNull
			public Mono<Void> insert(@NonNull ClientHttpRequest req, @NonNull Context context) {
				return request.body().insert(new ClientHttpRequestDecorator(req) {

					@Override
					@NonNull
					public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
						return super.writeWith(
								Flux.from(body).doOnNext(data -> capturedRequestBody.append(extractBytes(data)))); // number
																													// code
					}

				}, context);
			}
		}).build()).doOnNext(response -> {
			if (!requestLogged.getAndSet(true)) {
				log.info("| >>---> Outgoing {} request [{}]\n{} {}\n{}\n\n{}\n", externalSystem, clientRequestId,
						request.method(), request.url(), request.headers(), capturedRequestBody.toString() // filtered
																											// in real
																											// code
				);
			}
		}).doOnError(error -> {
			if (!requestLogged.getAndSet(true)) {
				log.info("| >>---> Outgoing {} request [{}]\n{} {}\n{}\n\nError: {}\n", externalSystem, clientRequestId,
						request.method(), request.url(), request.headers(), // filtered in real code
						error.getMessage());
			}
		}).map(response -> response.mutate()
				.body(transformer -> transformer.doOnNext(body -> capturedResponseBody.append(extractBytes(body))) // number
																													// of
																													// bytes
																													// appended
																													// is
																													// maxed
																													// in
																													// real
																													// code
						.doOnTerminate(() -> {
							if (stopWatch.isRunning()) {
								stopWatch.stop();
							}
						}).doOnComplete(() -> {
							if (!responseLogged.getAndSet(true)) {
								log.info("| <---<< Response for outgoing {} request [{}] after {}ms\n{} {}\n{}\n\n{}\n",
										externalSystem, clientRequestId, stopWatch.getTotalTimeMillis(),
										response.statusCode().value(), response.statusCode().getReasonPhrase(),
										response.headers(), capturedResponseBody.toString() // filtered in real code
								);
							}
						}).doOnError(error -> {
							if (!responseLogged.getAndSet(true)) {
								log.info("| <---<< Error parsing response for outgoing {} request [{}] after {}ms\n{}",
										externalSystem, clientRequestId, stopWatch.getTotalTimeMillis(),
										error.getMessage());
							}
						}))
				.build());
	}

	private static String extractBytes(DataBuffer data) {
		int currentReadPosition = data.readPosition();
		var numberOfBytesLogged = min(data.readableByteCount(), MAX_BYTES_LOGGED);
		var bytes = new byte[numberOfBytesLogged];
		data.read(bytes, 0, numberOfBytesLogged);
		data.readPosition(currentReadPosition);
		return new String(bytes);
	}

}