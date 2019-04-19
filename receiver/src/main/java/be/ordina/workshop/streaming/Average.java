package be.ordina.workshop.streaming;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Average {

	private int amountOfCars = 0;
	private int totalSpeed = 0;

	Average() {
	}

	@JsonCreator
	public Average(@JsonProperty("amountOfCars") int amountOfCars,
			@JsonProperty("totalSpeed") int totalSpeed) {
		this.amountOfCars = amountOfCars;
		this.totalSpeed = totalSpeed;
	}

	public int getAmountOfCars() {
		return this.amountOfCars;
	}

	public int getTotalSpeed() {
		return this.totalSpeed;
	}

	void addSpeed(int amountOfCars, int speed) {
		this.amountOfCars += amountOfCars;
		this.totalSpeed += speed;
	}

	double average() {
		if (this.amountOfCars == 0) {
			return 0;
		}
		return this.totalSpeed / this.amountOfCars;
	}

}
