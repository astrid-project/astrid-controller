package it.polito.contextbroker.model;

public class Connection {
	String description;
	String exec_env_id;
	String id;
	String network_link_id;
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getExec_env_id() {
		return exec_env_id;
	}
	public void setExec_env_id(String exec_env_id) {
		this.exec_env_id = exec_env_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNetwork_link_id() {
		return network_link_id;
	}
	public void setNetwork_link_id(String network_link_id) {
		this.network_link_id = network_link_id;
	}
}
