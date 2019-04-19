package be.ordina.workshop.streaming;

import be.ordina.workshop.streaming.domain.TrafficEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(Sink.class)
public class TrafficEventReceiver {

	private static final Logger logger =
			LoggerFactory.getLogger(TrafficEventReceiver.class);

	@StreamListener(Sink.INPUT)
	public void consumeEvent(Flux<TrafficEvent> events) {
		events.subscribe(event -> logger.info("Received event: {}", event));
	}

}
