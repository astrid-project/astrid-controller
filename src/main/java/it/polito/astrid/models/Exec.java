package it.polito.astrid.models;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;



public class Exec{
    @JsonProperty("execenvs") 
    public Execenvs getExecenvs() { 
		 return this.execenvs; } 
    public void setExecenvs(Execenvs execenvs) { 
		 this.execenvs = execenvs; } 
    public Execenvs execenvs;
    
    
    public  static class Node{
        @JsonProperty("id") 
        public String getId() { 
    		 return this.id; } 
        public void setId(String id) { 
    		 this.id = id; } 
        String id;
    }

    public static class Execenvs{
        @JsonProperty("lcp") 
        public int getLcp() { 
    		 return this.lcp; } 
        public void setLcp(int lcp) { 
    		 this.lcp = lcp; } 
        int lcp;
        @JsonProperty("namespace") 
        public String getNamespace() { 
    		 return this.namespace; } 
        public void setNamespace(String namespace) { 
    		 this.namespace = namespace; } 
        String namespace;
        @JsonProperty("nodes") 
        public List<Node> getNodes() { 
    		 return this.nodes; } 
        public void setNodes(List<Node> nodes) { 
    		 this.nodes = nodes; } 
        List<Node> nodes;
    }
}


