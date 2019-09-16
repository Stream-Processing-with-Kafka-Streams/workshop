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
* Define a `StreamListener`
* Set the destination of that listener to: `traffic-data`
* Just take in the events and print them out