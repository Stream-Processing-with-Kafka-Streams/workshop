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
[Send Events to Kafka](lab1.md)

### Lab 2: consume events from Kafka
[Consume Events from Kafka](lab2.md)

### Lab 3: use Kafka Streams, stateless
[Use Kafka Streams, stateless](lab3.md)

### Lab 4: make use of windows
[Use Windows with Kafka Streams](lab4.md)

### Lab 5: bonus - reactive ingestion
[Send Events Reactively With Spring Cloud Stream](lab5.md)

### Lab 6: Think about how you would detect a traffic congestion
Think about how you would tackle the problem.
In how many steps would you process your data?
