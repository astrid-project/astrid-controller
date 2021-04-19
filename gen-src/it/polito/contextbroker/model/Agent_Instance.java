package it.polito.contextbroker.model;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Agent_Instance {
	String id;
	String agent_catalog_id;
	String exec_env_id;
	List<actions> actions;
	
	public List<actions> getActions() {
		return actions;
	}
	public void setActions(List<actions> actions) {
		this.actions = actions;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAgent_catalog_id() {
		return agent_catalog_id;
	}
	public void setAgent_catalog_id(String agent_catalog_id) {
		this.agent_catalog_id = agent_catalog_id;
	}
	public String getExec_env_id() {
		return exec_env_id;
	}
	public void setExec_env_id(String exec_env_id) {
		this.exec_env_id = exec_env_id;
	}
}
