### Lab 4: Use Windows with Kafka Streams
Windows allow you to analyze your events over a certain timeframe.
Because often you don't want to see the latest state of a specific datapoint, but more a somewhat averaged output over a certain time window. 
It also gives your system time to deal with late events.
These are events which happened some time ago, but because of the will of the network latency gods did not arrive at the time they occurred.
Also it allows you to get a more weighted result as it allows you to average out certain spikes in your results.

#### Exercise

For this exercise we're going to continue from lab3.

We will be generating an average speed for a given sensor, so first define a result class which will be used to store the results per window.

```
    import com.fasterxml.jackson.annotation.JsonCreator;
    import com.fasterxml.jackson.annotation.JsonProperty;

    public class Average {

        private int amountOfCars = 0;
        private int totalSpeed = 0;

        Average() {
        }

        @JsonCreator
        public Average(@JsonProperty("amountOfCars") int amountOfCars,
                @JsonProperty("totalSpeed") int totalSpeed) {
            this.amountOfCars = amountOfCars;
            this.totalSpeed = totalSpeed;
        }

        public int getAmountOfCars() {
            return this.amountOfCars;
        }

        public int getTotalSpeed() {
            return this.totalSpeed;
        }

        void addSpeed(int amountOfCars, int speed) {
            this.amountOfCars += amountOfCars;
            this.totalSpeed += speed;
        }

        double average() {
            if (this.amountOfCars == 0) {
                return 0;
            }
            return this.totalSpeed / this.amountOfCars;
        }

    }
```

As soon as you start to make use of the statefull methods used by Kafka, Kafka Streams will store intermediate results in kafka topics, as you will see being printed out in your log statements.

For this exercise we will continue using `@StreamListener` and the `@Input(KstreamSink.INPUT)`, but we are going to change the entire existing implementation.

First filter out all `VehicleClass.CAR` using `.filter((k,v) -> {})`.

Then we are going to start with grouping, when grouping Kafka Streams will always group on the key, if you want to group on another value you must first set this.

Use `.selectKey((k,v) -> new_key)` to select a new key, make the `value.getSensorId` the new key.

Now we have a key and we can start grouping.
One very *important* aspect you should not forget is to pass along a correct `Serde`.
A `Serde` is used by Kafka to serialize and deserialize the data you send and retrieve from a topic.
Spring Cloud Stream automatically converts this to `JsonSerde` or when passing along a `String` to `Serdes.String`, but we will now need to provide this explicitly.
Use `.groupByKey(Serialized.with(Serdes.String(), new JsonSerde<>(TrafficEvent.class)))` to define a new grouping.

Important is when grouping, that Kafka Streams will write the output to new topics.
Where your newly defined key will be used to partition data.

Next we are going to define the windowing via `.windowedBy(TimeWindows.off(milliseconds))`, set the milliseconds for 2 minutes.

Now you have defined your windows we can aggregate our results over these windows.
An aggregate will always run over a certain key and return a `KTable` as result.

In this aggregate we will make use of the `Average.class` you defined previously, which will be used to group the average speedlimit for all cars passing a certain sensor in a 2 minute window.
You can see this as the reduce step in the MapReduce paradigm.

The [`<VR> KTable<K,VR> aggregate(Initializer<VR> initializer,Aggregator<? super K,? super V,VR> aggregator)`](https://kafka.apache.org/20/javadoc/org/apache/kafka/streams/kstream/KGroupedStream.html#aggregate-org.apache.kafka.streams.kstream.Initializer-org.apache.kafka.streams.kstream.Aggregator-) always needs an Initializer and an Aggregator.

The `Initializer` in this case is just `Average::new`.

The `Aggregator` is a method which takes in as parameters the key, value and initialized aggregator class, like `(sensorId, trafficEvent, average) -> {})`

In the method set the average speed `average.addSpeed(trafficEvent.getTrafficIntensity(),trafficEvent.getVehicleSpeedCalculated())` and return the aggregator `return average`.

To conclude the Aggregator we are going to define a third parameter: `Materialized<K,VR,KeyValueStore<org.apache.kafka.common.utils.Bytes,byte[]>> materialized` so we can make sure that the `Average` class will be serialized and deserialized property.
Add `Materialized.with(Serdes.String(), new JsonSerde<>(Average.class))` as third parameter.

Now that we have created an aggregation we will now retrieve the averages.

Use `.mapValues(Average::average)` to transform the values into the calculated average.

Then we can print out the results by first converting it to a `KStream` with `toStream` and then calling `.print(Printed.toSysOut())`

This should print out the results showing you the average car speed over a 2 minute window on the Belgium highway network.
Which can be either pretty fast or pretty slow depending on the time of the day.


#### The End Result
The end result should look something like this: 

```
    @StreamListener
	public void consumeEvent(@Input(KStreamSink.INPUT) KStream<String, TrafficEvent> stream) {
		stream.filter(((key, trafficEvent) -> VehicleClass.CAR == trafficEvent.getVehicleClass()))
				.selectKey((key, value) -> value.getSensorId())
				.groupByKey(Serialized.with(Serdes.String(), new JsonSerde<>(TrafficEvent.class)))
				.windowedBy(TimeWindows.of(120_000L))
				.aggregate(Average::new, (sensorId, trafficEvent, average) -> {
					average.addSpeed(trafficEvent.getTrafficIntensity(),
							trafficEvent.getVehicleSpeedCalculated());
					return average;
				}, Materialized.with(Serdes.String(), new JsonSerde<>(Average.class)))
				.mapValues(Average::average)
				.toStream()
				.print(Printed.toSysOut());
	}
```




