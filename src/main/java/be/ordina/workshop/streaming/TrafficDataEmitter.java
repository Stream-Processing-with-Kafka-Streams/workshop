package be.ordina.workshop.streaming;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import be.ordina.workshop.streaming.domain.TrafficEvent;
import be.ordina.workshop.streaming.domain.VehicleClass;
import generated.traffic.Miv;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuples;

import org.springframework.stereotype.Component;

/**
 * @author Tim Ysewyn
 */
@Component
public class TrafficDataEmitter {

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
				.map(data -> this.convertToTrafficEvent(data.getT1(), data.getT2()));
	}

	private TrafficEvent convertToTrafficEvent(Miv.Meetpunt meetpunt, Miv.Meetpunt.Meetdata meetdata) {
		Date lastUpdated = meetpunt.getTijdLaatstGewijzigd().toGregorianCalendar().getTime();
		boolean availableSensor = false;

		if (meetpunt.getBeschikbaar() == 1) {
			availableSensor = true;
		}

		boolean recentData = false;
		if (meetpunt.getActueelPublicatie() == 1) {
			recentData = true;
		}

		int lveNumber = meetpunt.getLveNr().intValue();

		Date timeRegistration = meetpunt.getTijdWaarneming().toGregorianCalendar().getTime();

		//trafficEvent.setSensorDefect();
		String sensorId = meetpunt.getUniekeId();
		String sensorDescriptiveId = meetpunt.getBeschrijvendeId();

		/* Handle the meetData data*/
		VehicleClass vehicleClass = getVehicleClassFromMeetData(meetdata);

		int trafficIntensity = meetdata.getVerkeersintensiteit();
		int vehicleSpeedCalculated = meetdata.getVoertuigsnelheidRekenkundig();
		int vehicleSpeedHarmonical = meetdata.getVoertuigsnelheidHarmonisch();

		return new TrafficEvent(vehicleClass, trafficIntensity, vehicleSpeedCalculated, vehicleSpeedHarmonical,
				sensorId, sensorDescriptiveId, lveNumber, timeRegistration, lastUpdated, recentData, availableSensor);
	}

	private VehicleClass getVehicleClassFromMeetData(Miv.Meetpunt.Meetdata meetdata) {
		return Arrays.stream(VehicleClass.values())
				.filter(e -> e.getValue() == meetdata.getKlasseId())
				.findFirst()
				.orElse(VehicleClass.UNKNOWN);
	}

}
