### Lab 4: Use Windows with Kafka Streams
Windows allow you to analyze your events over a certain timeframe.
Because often you don't want to see the latest state of a specific datapoint, but more a somewhat averaged output over a certain time window. 
It also gives your system time to deal with late events, events which happened some time ago, but because of the will of the network latency gods did not arrive at the time they occurred.

Because you are now going to convert your events and change its key, you may not forget to provide Kafka Streams with the Serializer / Deserializers it needs to use, the so-called serdes.

#### Consider the following:
* Make use of `GroupByKey`
* Define a `SerDe` for your value and your key, you can use the provided implementations to deal with String and JSON.
* Use count()
* Use `.toStream()` to convert the kTable output back to a Stream which you can then print out.

@StreamListener
	public void consumeEvent(@Input(KStreamSink.INPUT)
			KStream<String, TrafficEvent> stream) {
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

#### Exercise

For this exercise we're going to continue from lab3.

As soon as you start to make use of the statefull methods used by Kafka, Kafka Streams will store intermediate results in kafka topics, as you will see being printed out in your log statements.

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


For this exercise we will continue using `@StreamListener` and the `@Input(KstreamSink.INPUT`.

First filter out all `VehicleClass.CAR` using `.filter((k,v) -> {})`.

Then we are going to start with grouping, when grouping Kafka Streams will always group on the key, if you want to group on another value you must first set this.

Use `.selectKey((k,v) -> new_key)` to select a new key, make the `value.getSensorId` the new key.

Now we have a key and we kan start grouping, on very important aspect you should not forget is to pass along a correct `Serde`.
A `Serde` is used by Kafka to serialize and deserialize the data you send and retrieve from a topic.
Spring Cloud Stream automatically converts this to `JsonSerde` or when passing along a `String` to `Serdes.String`, but we will now need to provide this explicetely.
Use `.groupByKey(Serialized.with(Serdes.String(), new JsonSerde<>(TrafficEvent.class)))` to define a new grouping.


 


To make it easier, you might want to copy the `be.ordina.workshop.streaming.domain` package from the `sender` application to quickly bootstrap this exercise.

Next, let's start with a new `@Component` class named `TrafficEventReceiver`.
Like with the previous exercise we need to have a binding to our Kafka instance.
The only difference here is that we're going to use the `org.springframework.cloud.stream.messaging.Sink` interface because we're going to consume the messages.

This interface will have an `input` channel which we will use to receive the messages from the Kafka topic.
Create a new configuration file so we can specify which topic needs to be bound to the `input` channel.
Just add `spring.cloud.stream.bindings.input.destination=traffic-data` to it so everything is configured and we can focus on our code again!

Let's create a new method which will take a `TrafficEvent` as argument.
By annotating this method with `@StreamListener(Sink.INPUT)` you're wiring the `input` channel to this method so that it can process every event that the application gets from the Kafka topic.
To test this you can just log the `TrafficEvent` to stdout.