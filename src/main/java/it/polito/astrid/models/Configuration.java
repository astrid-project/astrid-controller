package it.polito.astrid.models;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString), Root.class); */

@JsonInclude(JsonInclude.Include.NON_EMPTY) 
public class Configuration{
	@JsonProperty("deployment") 
    public Deployment getDeployment() { 
		 return this.deployment; } 
    public void setDeployment(Deployment deployment) { 
		 this.deployment = deployment; } 
    Deployment deployment;
    
    
    
    
    
    
    
    
    
    public static class Operation{
        @JsonProperty("id") 
        public String getId() { 
    		 return this.id; } 
        public void setId(String id) { 
    		 this.id = id; } 
        String id;
        @JsonProperty("type") 
        public String getType() { 
    		 return this.type; } 
        public void setType(String type) { 
    		 this.type = type; } 
        String type;
        @JsonProperty("value") 
        public String getValue() { 
    		 return this.value; } 
        public void setValue(String value) { 
    		 this.value = value; } 
        String value;
        @JsonProperty("mode") 
        public int getMode() { 
    		 return this.mode; } 
        public void setMode(int mode) { 
    		 this.mode = mode; } 
        int mode;
    }

    public static  class Agent{
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
        @JsonProperty("exec_env_id") 
        public String getExec_env_id() { 
    		 return this.exec_env_id; } 
        public void setExec_env_id(String exec_env_id) { 
    		 this.exec_env_id = exec_env_id; } 
        String exec_env_id;
        @JsonProperty("operations") 
        public List<Operation> getOperations() { 
    		 return this.operations; } 
        public void setOperations(List<Operation> operations) { 
    		 this.operations = operations; } 
        List<Operation> operations;
    }
    public static  class Pipeline{
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
        @JsonProperty("status") 
        public String getStatus() { 
    		 return this.status; } 
        public void setStatus(String status) { 
    		 this.status = status; } 
        String status;
        @JsonProperty("agents") 
        public List<Agent> getAgents() { 
    		 return this.agents; } 
        public void setAgents(List<Agent> agents) { 
    		 this.agents = agents; } 
        List<Agent> agents;
        @JsonProperty("algorithms") 
        public List<Object> getAlgorithms() { 
    		 return this.algorithms; } 
        public void setAlgorithms(List<Object> algorithms) { 
    		 this.algorithms = algorithms; } 
        List<Object> algorithms;
        @JsonProperty("configuration") 
        public String getConfiguration() { 
    		 return this.configuration; } 
        public void setConfiguration(String configuration) { 
    		 this.configuration = configuration; } 
        String configuration;
    }

    public static  class Deployment{
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
}

