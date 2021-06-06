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

 
 class CorrelationAreaStatus{
    @JsonProperty("use-case") 
    public String useCase;
    public List<Correlation> correlations;
}

public class NetworkStatus{
    @JsonProperty("network-status") 
    public String networkStatus;
    public String mUseCase;
    public String getmUseCase() {
		return mUseCase;
	}
	public void setmUseCase(String mUseCase) {
		this.mUseCase = mUseCase;
	}
	@JsonProperty("correlation-type-status") 
    public List<CorrelationTypeStatus> correlationTypeStatus;
    @JsonProperty("correlation-area-status") 
    public List<CorrelationAreaStatus> correlationAreaStatus;
    public int time;
}