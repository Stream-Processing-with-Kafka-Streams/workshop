package be.ordina.workshop.streaming.domain;


public class SensorData {

    private String uniqueId;

    /*
    MeetpuntId
    */
    private Integer sensorId;
    /*
    Meetpunt beschrijvende Id
     */
    private String sensorDescriptiveId;

    private String name;

    /*
    Unique road number.
        More info in the dataset of numbered roads in the "Wegenregister" (Roads registry), field: locatieide,
        http://opendata.vlaanderen.be/dataset/wegenregister-15-09-2016
        Or the dataset "De beheersegmenten van de genummerde wegen" by AWV, field ident8,
        http://www.geopunt.be/catalogus/datasetfolder/12b65bc0-8c71-447a-8285-3334ca1769d8
    */
    private String ident8;

    /*
    Reference to the lane of the measurement point.
      The character indicates the lane type.
        R: Regular lane
        B: Bus lane or similar
        TR: measurement of the traffic in the opposite direction (p.e. in or near tunnels) on the corresponding R-lane.
        P: Hard shoulder lane
        W: parking or other road
        S: Lane for hard shoulder running
        A: Hatched area

      Counting starts at R10 for the first regular lane of the main road. Lane numbers increase from right/slower to left/faster lanes.
      Lanes 09, 08, 07, ... are positioned right of this first lane, and mainly indicate access/merging lanes, deceleration lanes, recently added lanes, lanes for hard shoulder running, bus lanes
      Lanes 11, 12, 13, ... are positioned left of lane R10.
      The lane number 00 is used for measurement points on the hard shoulder (P00).
      The TR-lane is identical to the corresponding R-lane (TR10=R10,TR11=R11,TR12=R12,...), but returns the data of the "ghost traffic" instead.
      (The data for TR10 and R10 are provided by the same detection loops.)
     */
    private String trafficLane;

    public SensorData(String uniqueId, String sensorDescriptiveId, String name, String ident8, String trafficLane) {
        this.uniqueId = uniqueId;
        this.sensorDescriptiveId = sensorDescriptiveId;
        this.name = name;
        this.ident8 = ident8;
        this.trafficLane = trafficLane;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public Integer getSensorId() {
        return sensorId;
    }

    public String getSensorDescriptiveId() {
        return sensorDescriptiveId;
    }

    public String getName() {
        return name;
    }

    public String getIdent8() {
        return ident8;
    }

    public String getTrafficLane() {
        return trafficLane;
    }
}
