package it.polito.astrid.models;

public class KafkaMessage {
	private String source_ip;
	private String source_port;
	private String destination_ip;
	private String destination_port;
	private String protocol;
	private String timestamp;
	private String attack;
	
	public String getSource_ip() {
		return source_ip;
	}
	public void setSource_ip(String source_ip) {
		this.source_ip = source_ip;
	}
	public String getSource_port() {
		return source_port;
	}
	public void setSource_port(String source_port) {
		this.source_port = source_port;
	}
	public String getDestination_ip() {
		return destination_ip;
	}
	public void setDestination_ip(String destination_ip) {
		this.destination_ip = destination_ip;
	}
	public String getDestination_port() {
		return destination_port;
	}
	public void setDestination_port(String destination_port) {
		this.destination_port = destination_port;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getAttack() {
		return attack;
	}
	public void setAttack(String attack) {
		this.attack = attack;
	}
	
}
