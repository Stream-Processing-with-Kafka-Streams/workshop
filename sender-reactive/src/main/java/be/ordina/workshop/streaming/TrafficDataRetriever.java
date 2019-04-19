package be.ordina.workshop.streaming;

import java.io.ByteArrayInputStream;
import java.util.Objects;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import generated.traffic.Miv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Tim Ysewyn
 */
@Component
public class TrafficDataRetriever {

	private static final Logger logger = LoggerFactory.getLogger(TrafficDataRetriever.class);

	private final String baseUrl;

	public TrafficDataRetriever(@Value("${ws.trafficdata.baseUrl:}") String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public Mono<Miv> getTrafficData() {
		return WebClient.create(this.baseUrl)
				.get().uri("/miv/verkeersdata")
				.retrieve()
				.bodyToMono(String.class)
				.map(this::convertToMivObject)
				.filter(Objects::nonNull);
	}

	private Miv convertToMivObject(String xml) {
		Miv miv = null;
		try {
			JAXBContext jc = JAXBContext.newInstance("generated.traffic");
			Unmarshaller um = jc.createUnmarshaller();
			miv = (Miv) um.unmarshal(new ByteArrayInputStream(xml.getBytes()));
		} catch (JAXBException je) {
			logger.error("An error occurred while retrieving the traffic data: {}", je);
		}
		return miv;
	}

}
