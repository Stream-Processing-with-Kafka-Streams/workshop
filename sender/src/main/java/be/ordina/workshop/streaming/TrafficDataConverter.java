package be.ordina.workshop.streaming;

import java.util.Arrays;
import java.util.Date;

import be.ordina.workshop.streaming.domain.TrafficEvent;
import be.ordina.workshop.streaming.domain.VehicleClass;
import generated.traffic.Miv;
import reactor.util.function.Tuple2;

/**
 * @author Tim Ysewyn
 */
class TrafficDataConverter {

	TrafficEvent convertToTrafficEvent(Tuple2<Miv.Meetpunt, Miv.Meetpunt.Meetdata> data) {
		Miv.Meetpunt meetpunt = data.getT1();
		Miv.Meetpunt.Meetdata meetdata = data.getT2();

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
