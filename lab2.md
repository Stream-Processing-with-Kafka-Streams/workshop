### Lab 2: consume events from Kafka
With this exercise we will let you consume your first messages from Kafka by making use of Spring Cloud Stream.
This is the most basic form of stream processing, your method will be called for every event present on the topic.
Events will only be consumed from the moment the consumer connected first to your Kafka (at that moment it will register its consumer offset)
So if you do not see immediate results ... wait a minute.

#### Pay attention to the following:
* Go to start.spring.io and create a new project, add the following starters:
    * Cloud Stream
    * Spring for Apache Kafka
* Store the generated project as a zipfile.

* Use `enablebinding` to define your input

Define a `StreamListener`, in this class we will inject a `Sink`, once we have injected this bean we can use this to receive events via the `input` channel.


To do this we'll have to specify in our properties file where we want to have our `input` channel bind to.
Just add `spring.cloud.stream.bindings.input.destination=traffic-data` and we're ready to send the events!


* Just take in the events and print them out