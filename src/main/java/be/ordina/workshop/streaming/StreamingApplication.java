package be.ordina.workshop.streaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.test.rule.KafkaEmbedded;

@SpringBootApplication
public class StreamingApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamingApplication.class, args);
	}

	@Bean
	public KafkaEmbedded embeddedKafka() {
		KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, 1);
		embeddedKafka.setKafkaPorts(9092);
		return embeddedKafka;
	}

}
