### Lab 2: consume events from Kafka
#### Goal
With this exercise we will let you consume your first messages from Kafka by making use of Spring Cloud Stream.
This is the most basic form of stream processing, your method will be called for every event present on the topic.
Events will only be consumed from the moment the consumer connected first to your Kafka (at that moment it will register its consumer offset)
So if you do not see immediate results ... wait a minute.

#### Exercise

For this exercise we're going to visit <a href="https://start.spring.io" target="_blank">start.spring.io</a>.
You can configure anything what you want, but just make sure that you add `Cloud Stream` and `Spring for Apache Kafka` as dependencies to your project.
Once you hit generate a new project will be created, zipped and downloaded to your computer.
Make sure not to delete the zip file if you want to create separate applications for the next labs.

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
