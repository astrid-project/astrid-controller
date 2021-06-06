package it.polito.contextbroker.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Execution_Environment {
	String id;
	String hostname;
	String type_id;
	LCP lcp;
	String description;
	String enabled;
	
	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public LCP getLcp() {
		return lcp;
	}

	public void setLcp(LCP lcp) {
		this.lcp = lcp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public class LCP{
		Integer port;
		Date started;
		Date last_heartbeat;
		String username;
		String password;
		 boolean https;
		
		public boolean isHttps() {
			return https;
		}
		public void setHttps(boolean https) {
			this.https = https;
		}
		public Integer getPort() {
			return port;
		}
		public void setPort(Integer port) {
			this.port = port;
		}
		public Date getStarted() {
			return started;
		}
		public void setStarted(Date started) {
			this.started = started;
		}
		public Date getLast_heartbeat() {
			return last_heartbeat;
		}
		public void setLast_heartbeat(Date last_heartbeat) {
			this.last_heartbeat = last_heartbeat;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
	}
}
