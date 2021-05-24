package it.polito.astrid.models;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString), Root.class); */
 class Correlation{
    @JsonProperty("node-type") 
    public String nodeType;
    public String value;
    public String area;
}

class CorrelationTypeStatu{
    @JsonProperty("use-case") 
    public String useCase;
    public List<Correlation> correlations;
}

 class CorrelationAreaStatu{
    @JsonProperty("use-case") 
    public String useCase;
    public List<Correlation> correlations;
}

public class NetworkStatus{
    @JsonProperty("network-status") 
    public String networkStatus;
    @JsonProperty("correlation-type-status") 
    public List<CorrelationTypeStatu> correlationTypeStatus;
    @JsonProperty("correlation-area-status") 
    public List<CorrelationAreaStatu> correlationAreaStatus;
    public int time;
}