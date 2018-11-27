package be.ordina.workshop.streaming;

import java.util.List;
import java.util.function.Function;

import be.ordina.workshop.streaming.domain.TrafficEvent;
import generated.traffic.Miv;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

import org.springframework.stereotype.Component;

/**
 * @author Tim Ysewyn
 */
@Component
public class TrafficDataEmitter {

	private final TrafficDataConverter trafficDataConverter = new TrafficDataConverter();
	private final TrafficDataRetriever trafficDataRetriever;

	public TrafficDataEmitter(TrafficDataRetriever trafficDataRetriever) {
		this.trafficDataRetriever = trafficDataRetriever;
	}

	private List<TrafficEvent> getTrafficDataEventsAsList() {
		return this.getTrafficDataEvents().collectList().block();
	}

	private Flux<TrafficEvent> getTrafficDataEvents() {
		return this.trafficDataRetriever.getTrafficData()
				.map(Miv::getMeetpunt)
				.flatMapIterable(Function.identity())
				.flatMap(meetpunt ->
					Flux.fromStream(meetpunt.getMeetdata().stream()
							.map(meetdata -> Tuples.of(meetpunt, meetdata))))
				.map(trafficDataConverter::convertToTrafficEvent);
	}

}
