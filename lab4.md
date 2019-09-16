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