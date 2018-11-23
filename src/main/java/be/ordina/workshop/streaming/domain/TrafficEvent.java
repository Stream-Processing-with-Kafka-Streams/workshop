package be.ordina.workshop.streaming.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TrafficEvent {

    private VehicleClass vehicleClass;
    /*
    This is the vehicleCount.
     */
    private int trafficIntensity;

    /*
    Sum (vi) / n = arithmetic average speed of the vehicles in this vehicle class
        (with vi = individual speed of a vehicle in this vehicle class)
        Value domaing 0 to 254 km/h.
        Value range 0..200 km/h
        Resolution 1.
        Special values:
        - 251: Initial value
        - 254: Calculation not possible
        - 252: no vehicles were counted in this vehicle class.
     */
    private int vehicleSpeedCalculated;

    /*
    n / Sum (1/vi) = harmonic average speed of the vehicles in this vehicle class
         (with vi = individual speed of a vehicle in this vehicle class)
        Special values:
        - 251: Initial value
        - 254: Calculation not possible
        - 252: no vehicles were counted in this vehicle class.
     */
    private int vehicleSpeedHarmonical;

    /*
    MeetpuntId
     */
    private String sensorId;
    /*
    Meetpunt beschrijvende Id
     */
    private String sensorDescriptiveId;

    private int lveNumber;
    private Date timeRegistration;
    private Date lastUpdated;

    /*
    actueel_publicatie: 1 = data is minder dan 3 minuten oud.
     */
    private boolean recentData;

    private boolean availableMeetpunt;

    private Integer sensorDefect;
    private Integer sensorValid;

    @JsonCreator
    public TrafficEvent(@JsonProperty("vehicleClass") VehicleClass vehicleClass,
                        @JsonProperty("trafficIntensity") int trafficIntensity,
                        @JsonProperty("vehicleSpeedCalculated") int vehicleSpeedCalculated,
                        @JsonProperty("vehicleSpeedHarmonical") int vehicleSpeedHarmonical,
                        @JsonProperty("sensorId") String sensorId,
                        @JsonProperty("sensorDescriptiveId") String sensorDescriptiveId,
                        @JsonProperty("lveNumber") int lveNumber,
                        @JsonProperty("timeRegistration") Date timeRegistration,
                        @JsonProperty("lastUpdated") Date lastUpdated,
                        @JsonProperty("recentData") boolean recentData,
                        @JsonProperty("availableMeetpunt") boolean availableMeetpunt) {
        this.vehicleClass = vehicleClass;
        this.trafficIntensity = trafficIntensity;
        this.vehicleSpeedCalculated = vehicleSpeedCalculated;
        this.vehicleSpeedHarmonical = vehicleSpeedHarmonical;
        this.sensorId = sensorId;
        this.sensorDescriptiveId = sensorDescriptiveId;
        this.lveNumber = lveNumber;
        this.timeRegistration = timeRegistration;
        this.lastUpdated = lastUpdated;
        this.recentData = recentData;
        this.availableMeetpunt = availableMeetpunt;
    }

    public VehicleClass getVehicleClass() {
        return vehicleClass;
    }

    public int getTrafficIntensity() {
        return trafficIntensity;
    }

    public int getVehicleSpeedCalculated() {
        return vehicleSpeedCalculated;
    }

    public int getVehicleSpeedHarmonical() {
        return vehicleSpeedHarmonical;
    }

    public String getSensorId() {
        return sensorId;
    }

    public String getSensorDescriptiveId() {
        return sensorDescriptiveId;
    }

    public int getLveNumber() {
        return lveNumber;
    }

    public Date getTimeRegistration() {
        return timeRegistration;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public boolean getRecentData() {
        return recentData;
    }

    public boolean getAvailableMeetpunt() {
        return availableMeetpunt;
    }

    public Integer getSensorDefect() {
        return sensorDefect;
    }

    public Integer getSensorValid() {
        return sensorValid;
    }
}
