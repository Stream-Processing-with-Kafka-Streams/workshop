# stream-processing-workshop

## Get Started
1. Use java 8
2. Get the following [command](http://bit.ly/docker-kafka) so you can get a kafka / zookeeper running locally via docker.
3. Use maven build to generate some java classes:
```
  > mvn clean compile
```

### If you don't have Docker
You can get kafka and run it locally

Go to the kafka [quickstart](https://kafka.apache.org/quickstart):
1. Download the [latest](https://www.apache.org/dyn/closer.cgi?path=/kafka/2.2.0/kafka_2.12-2.2.0.tgz)
2. Start Zookeeper (Kafka provides you with a single node zookeeper instance)
```
  > bin/zookeeper-server-start.sh config/zookeeper.properties
```
3. start Kafka
```
  > bin/kafka-server-start.sh config/server.properties
```

That will get your kafka running on the default port: localhost:9092 and zookeeper on: localhost:2181
These are also the ports used by default by Spring Cloud Stream

## Labs

### Lab 1: send events to Kafka
In this first exercise we will take in the traffic data every minute.
That data will be chopped into events by the existing, provided code.
After which you will have to put these new events onto a kafka topic traffic-data.

For this we will make use of Spring Cloud Stream.

#### Consider the following

We will start from the existing project sender, which we will use to read in the data, tansform these into events and put these in a kafka topic.
* Add the spring cloud stream starter for kafka: `spring-cloud-starter-stream-kafka`
* Use `enableBinding` to define your output
* Set the destination of your output to: `traffic-data`
* Use `Scheduling` to retrieve the traffic data from the external source every minute

### Lab 2: consume events from Kafka
With this exercise we will let you consume your first messages from Kafka by making use of Spring Cloud Stream.
This is the most basic form of stream processing, your method will be called for every event present on the topic.
Events will only be consumed from the moment the consumer connected first to your Kafka (at that moment it will register its consumer offset)
So if you do not see immediate results ... wait a minute.

#### Pay attention to the following:
* Use `enablebinding` to define your input
* Define a `StreamListener`
* Set the destination of that listener to: `traffic data`
* Just take in the events and print them out

### Lab 3: use Kafka Streams, stateless
Now we will start using Kafka Streams in order to process the events, so that you can make use of all the pre-implemented streaming operations.

Filter out all the cars and print these out.

#### Consider the following:
* Take in the kafka steams binder `spring-cloud-stream-binder-kafka-streams`
* Define your own custom binding interface
* Use the methods `.filter()` and `.print()`
* Set the destination of your new binder
* You will need to give up a default binder (because you now have 2 in your config and Spring Cloud Stream does not know which to use when)

### Lab 4: make use of windows
Windows allow you to analyze your events over a certain timeframe.
Because often you don't want to see the latest state of a specific datapoint, but more a somewhat averaged output over a certain time window. 
It also gives your system time to deal with late events, events which happened some time ago but because of the will of the network latency gods did not arrive at the time they occurred.

Because you are now going to convert your events and change its key, you may not forget to provide Kafka Streams with the Serializer / Deserializers it needs to use, the so-called serdes.

#### Consider the following:
* Make use of `GroupByKey`
* Define a `SerDe` for your value and your key, you can use the provided implementations to deal with String and JSON.
* Use count()
* Use `.toStream()` to convert the kTable output back to a Stream which you can then print out.

### Lab 5: bonus - reactive ingestion

It is also possible to make use of reactive programming within Spring Cloud Stream.
In this exercise you will update the code of exercise 1 by making it reactive.

#### Consider the following:
* At the spring cloud stream dependency for reactive
* Make use of a `StreamEmitter`
* Use a `SendTo`

### Lab 6: Think about how you would detect a traffic congestion
Think about how you would tackle the problem.
In how many steps would you process your data?
