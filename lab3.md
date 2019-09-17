### Lab 3: use Kafka Streams, stateless
#### Goal
Now we will convert our application to use the native Kafka streams functionality.
This exercise will cover the stateless operations which we'll use to filter out all the cars and print them out.

#### Exercise
You might want to make a copy of the application so you can see the difference between native Kafka stream processing and Spring Cloud Stream, but in this exercise we won't do that so we can show you the power of multiple binders on the classpath.

To start we'll add a new dependency `spring-cloud-stream-binder-kafka-streams` to enable the native Kafka stream binder.
Now that we added this new binder our application won't be able to start anymore.
Behind the scenes Spring Cloud Stream will look at a `spring.binders` file to determine which binders are available.
If there is only one binder everything is fine, but if there are more we'll have to explicitly configure which binder to use.
To fix this we're going to add `spring.cloud.stream.default-binder=kafka` to our properties.

Everything should work again, but just to be sure, let's (re)start our application. 

Great! Now we need to create a new binding between our application and our Kafka broker using the newly added binder.
Instead of using the default `Sink` we're going to create our own one.
Start with creating a new interface named `KStreamSink`.
Just like the `Sink` interface we need to create a new input channel:
```
public interface KStreamSink {

	String INPUT = "native-input";

	@Input(INPUT)
	KStream<String, TrafficEvent> input();
}
```

Notice the `@Input(INPUT)` annotation?
This will be used as our channel name.
Now we need to connect this channel to our topic.
Add `spring.cloud.stream.bindings.native-input.destination=traffic-data` to your properties.
You might think that we're done here but do you remember that we set our default binder at the beginning of the exercise?
To be able to use the `KStream<String, TrafficEvent>` input channel we need to change the binder for this channel:
```
spring.cloud.stream.bindings.native-input.binder=kstream
```

Alright, we're all set to create some code again!

In our `TrafficEventReceiver` component we're going to add a new `@StreamListener`.
You might want to comment out the existing `consumeEvent` method to you don't log the events twice.
As a starting point we're going to add a new `consumeEvent` method:
```
@StreamListener
public void consumeEvent(@Input(KStreamSink.INPUT)
        KStream<String, TrafficEvent> stream) {
}
```
Instead of passing `KStreamSink.INPUT` as an argument to `@StreamListener` we're going to add the `@Input(KStreamSink.INPUT)` annotation to our method argument.
This way we can add multiple `@Input`s and `@Output`s to our method so we can aggregate multiple inputs for example.

To complete this exercise we need to add following to our `consumeEvent` method:
```
stream.filter(((key, trafficEvent) -> VehicleClass.CAR == trafficEvent.getVehicleClass()))
      .print(Printed.toSysOut());
```
