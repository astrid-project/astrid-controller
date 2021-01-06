package it.polito.astrid.models;

public class KafkaCounter {
	public int counter;
	
	public KafkaCounter() {
		counter = 0;
	}
	
	public void incrementKafkaCounter() {
		counter++;
	}
	
	public void decrementKafkaCounter() {
		counter--;
	}
	
	public int getKafkaCounter() {
		return counter;
	}
}
