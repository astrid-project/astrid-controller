package it.polito.contextbroker.model;

import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString), Root.class); */

public class Ebpf {
	@JsonProperty("description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	String description;

	@JsonProperty("ebpf_program_catalog_id")
	public String getEbpf_program_catalog_id() {
		return this.ebpf_program_catalog_id;
	}

	public void setEbpf_program_catalog_id(String ebpf_program_catalog_id) {
		this.ebpf_program_catalog_id = ebpf_program_catalog_id;
	}

	String ebpf_program_catalog_id;

	@JsonProperty("exec_env_id")
	public String getExec_env_id() {
		return this.exec_env_id;
	}

	public void setExec_env_id(String exec_env_id) {
		this.exec_env_id = exec_env_id;
	}

	String exec_env_id;

	@JsonProperty("id")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	String id;

	@JsonProperty("interface")
	public String getInterface() {
		return this.interfaces;
	}

	public void setInterface(String interfaces) {
		this.interfaces = interfaces;
	}

	String interfaces;
}
