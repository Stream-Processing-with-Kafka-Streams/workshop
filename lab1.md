### Lab 1: send events to Kafka
#### Goal

In this first exercise we will take in the traffic data every minute.
That data will be chopped into events by the existing, provided code.
After which you will have to put these new events onto a kafka topic `traffic-data`.

For this we will make use of Spring Cloud Stream.

#### Exercise

We've already set up a project named `sender` which will read the data from the available HTTP endpoint, and convert it to the proper events.
In order to be able to connect to Kafka and send the events to the topic we'll need to do a couple of things first.

Let's start by adding the mandatory dependencies to the pom.xml file which is `spring-cloud-starter-stream-kafka`.

Next up we'll need to enable the binding from our application to the Kafka broker.
Remember that our application is the source of the events, so we'll use the provided `org.springframework.cloud.stream.messaging.Source` interface towards Kafka.
To enable this binding we'll have to use the `@EnableBinding` annotation which you can add to one of the configuration classes or the `TrafficDataEmitter` component.

Because the exposed data is being refreshed every minute, we'll make use of the scheduling mechanism with a fixed rate of 60 seconds.
Add following method to the `TrafficDataEmitter` component as a starting point.
```
@Scheduled(fixedRate = 60_000L)
public void sendTrafficEvents() {
}
```

Note: Don't forget to add the `@EnableScheduling` annotation, otherwise this method will never be executed.

Remember that `Source` interface we used for the binding?
We need to adapt the constructor of our `TrafficDataEmitter` class so a `Source` bean is being injected.
Once we have this bean injected we'll be able to send events using its `output` channel.
This `output` channel is our internal application channel which will be bound to our Kafka topic.

To do this we'll have to specify in our properties file where we want to have our `output` channel bind to.
Just add `spring.cloud.stream.bindings.output.destination=traffic-data` and we're ready to send the events!

The only thing to do now is to create the actual implementation of the `sendTrafficEvents` method:
```
this.getTrafficDataEventsAsList().stream()
    .map(trafficEvent -> MessageBuilder.withPayload(trafficEvent).build())
    .forEach(message -> this.source.output().send(message));
```
In the code above we're going to convert our list of events to a stream and map every event to a Spring Integration `Message`, so we can send the message to our `output` channel.