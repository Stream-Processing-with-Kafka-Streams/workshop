package be.ordina.workshop.streaming;

import java.util.function.Function;

import generated.traffic.Miv;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

import be.ordina.workshop.streaming.domain.TrafficEvent;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.reactive.StreamEmitter;
import org.springframework.stereotype.Component;

/**
 * @author Tim Ysewyn
 */
@Component
@EnableBinding(Source.class)
public class TrafficDataEmitter {

	private final TrafficDataConverter trafficDataConverter = new TrafficDataConverter();
	private final TrafficDataRetriever trafficDataRetriever;

	public TrafficDataEmitter(TrafficDataRetriever trafficDataRetriever) {
		this.trafficDataRetriever = trafficDataRetriever;
	}

	@StreamEmitter
	@Output(Source.OUTPUT)
	public Flux<TrafficEvent> sendTrafficEvents() {
		return this.trafficDataRetriever.getTrafficData()
				.map(Miv::getMeetpunt)
				.flatMapIterable(Function.identity())
				.flatMap(meetpunt ->
					Flux.fromStream(meetpunt.getMeetdata().stream()
							.map(meetdata -> Tuples.of(meetpunt, meetdata))))
				.map(trafficDataConverter::convertToTrafficEvent);
	}

}
