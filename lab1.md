### Lab 1: send events to Kafka
In this first exercise we will take in the traffic data every minute.
That data will be chopped into events by the existing, provided code.
After which you will have to put these new events onto a kafka topic traffic-data.

For this we will make use of Spring Cloud Stream.

#### Consider the following

We will start from the existing project sender, which we will use to read in the data, tansform these into events and put these in a kafka topic.
* Add the spring cloud stream starter for kafka to your pom.xml: `spring-cloud-starter-stream-kafka`
* Use `@EnableBinding` to define your output
* Set the destination of your output to: `traffic-data`
* Use `Scheduling` to retrieve the traffic data from the external source every minute