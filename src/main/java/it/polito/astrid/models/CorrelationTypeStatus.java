package it.polito.astrid.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CorrelationTypeStatus{
    @JsonProperty("use-case") 
    public String useCase;
    public String getUseCase() {
		return useCase;
	}
	public void setUseCase(String useCase) {
		this.useCase = useCase;
	}
	public List<Correlation> correlations;
}