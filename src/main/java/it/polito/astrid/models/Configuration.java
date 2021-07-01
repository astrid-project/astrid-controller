package it.polito.astrid.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;




public class Configuration{
	
	public static class Argument{
		 @JsonProperty("name") 
		 public String getName() { 
				 return this.name; } 
		 public void setName(String name) { 
				 this.name = name; } 
		 String name;
		 @JsonProperty("type") 
		 public String getType() { 
				 return this.type; } 
		 public void setType(String type) { 
				 this.type = type; } 
		 String type;
		 @JsonProperty("value") 
		 public Object getValue() { 
				 return this.value; } 
		 public void setValue(Object value) { 
				 this.value = value; } 
		 Object value;
		}

		public static class Agent{
		 @JsonProperty("id") 
		 public String getId() { 
				 return this.id; } 
		 public void setId(String id) { 
				 this.id = id; } 
		 String id;
		 @JsonProperty("name") 
		 public String getName() { 
				 return this.name; } 
		 public void setName(String name) { 
				 this.name = name; } 
		 String name;
		 @JsonProperty("ip") 
		 public String getIp() { 
				 return this.ip; } 
		 public void setIp(String ip) { 
				 this.ip = ip; } 
		 String ip;
		 @JsonProperty("port") 
		 public int getPort() { 
				 return this.port; } 
		 public void setPort(int port) { 
				 this.port = port; } 
		 int port;
		 @JsonProperty("arguments") 
		 public List<Argument> getArguments() { 
				 return this.arguments; } 
		 public void setArguments(List<Argument> arguments) { 
				 this.arguments = arguments; } 
		 List<Argument> arguments;
		}

		public static class Algorithm{
		 @JsonProperty("id") 
		 public String getId() { 
				 return this.id; } 
		 public void setId(String id) { 
				 this.id = id; } 
		 String id;
		 @JsonProperty("name") 
		 public String getName() { 
				 return this.name; } 
		 public void setName(String name) { 
				 this.name = name; } 
		 String name;
		 @JsonProperty("arguments") 
		 public List<Argument> getArguments() { 
				 return this.arguments; } 
		 public void setArguments(List<Argument> arguments) { 
				 this.arguments = arguments; } 
		 List<Argument> arguments;
		}

		public static class Pipeline{
		 @JsonProperty("id") 
		 public String getId() { 
				 return this.id; } 
		 public void setId(String id) { 
				 this.id = id; } 
		 String id;
		 @JsonProperty("name") 
		 public String getName() { 
				 return this.name; } 
		 public void setName(String name) { 
				 this.name = name; } 
		 String name;
		 @JsonProperty("agents") 
		 public List<Agent> getAgents() { 
				 return this.agents; } 
		 public void setAgents(List<Agent> agents) { 
				 this.agents = agents; } 
		 List<Agent> agents;
		 @JsonProperty("algorithms") 
		 public List<Algorithm> getAlgorithms() { 
				 return this.algorithms; } 
		 public void setAlgorithms(List<Algorithm> algorithms) { 
				 this.algorithms = algorithms; } 
		 List<Algorithm> algorithms;
		 @JsonProperty("configuration") 
		 public String getConfiguration() { 
				 return this.configuration; } 
		 public void setConfiguration(String configuration) { 
				 this.configuration = configuration; } 
		 String configuration;
		}

		public static class Deployment{
		 @JsonProperty("id") 
		 public String getId() { 
				 return this.id; } 
		 public void setId(String id) { 
				 this.id = id; } 
		 String id;
		 @JsonProperty("name") 
		 public String getName() { 
				 return this.name; } 
		 public void setName(String name) { 
				 this.name = name; } 
		 String name;
		 @JsonProperty("namespace") 
		 public String getNamespace() { 
				 return this.namespace; } 
		 public void setNamespace(String namespace) { 
				 this.namespace = namespace; } 
		 String namespace;
		 @JsonProperty("pipelines") 
		 public List<Pipeline> getPipelines() { 
				 return this.pipelines; } 
		 public void setPipelines(List<Pipeline> pipelines) { 
				 this.pipelines = pipelines; } 
		 List<Pipeline> pipelines;
		}
	
 @JsonProperty("deployment") 
 public Deployment getDeployment() { 
		 return this.deployment; } 
 public void setDeployment(Deployment deployment) { 
		 this.deployment = deployment; } 
 Deployment deployment;
}

