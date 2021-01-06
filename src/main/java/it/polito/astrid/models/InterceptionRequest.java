package it.polito.astrid.models;

import org.springframework.http.ResponseEntity;

import it.polito.verefoo.astrid.jaxb.InfrastructureEvent;
import it.polito.verefoo.astrid.jaxb.InfrastructureInfo;
import it.polito.verefoo.jaxb.NFV;

public class InterceptionRequest {
    String userId;
    String providerId;
    String serviceId;
    String command;
    InfrastructureInfo info;
    String policy;
    InfrastructureEvent event;
    ResponseEntity<NFV> nfv;
    ResponseEntity<String> result;
    KafkaMessage mess;

    public KafkaMessage getMess() {
		return mess;
	}

	public void setMess(KafkaMessage mess) {
		this.mess = mess;
	}

	public InterceptionRequest(String userId, String providerId, String serviceId, String command, InfrastructureInfo info, String policy) {
        this.userId = userId;
        this.providerId = providerId;
        this.serviceId = serviceId;
        this.command = command;
        this.info = info;
        this.policy = policy;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
    
    public  InfrastructureInfo getInfo() {
        return info;
    }

    public void setInfo(InfrastructureInfo info) {
        this.info = info;
    }
    
    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }
    
    public InfrastructureEvent getEvent() {
        return event;
    }

    public void setEvent(InfrastructureEvent event) {
        this.event = event;
    }
    
    public ResponseEntity<NFV> getNfv() {
        return nfv;
    }

    public void setNfv(ResponseEntity<NFV> nfv) {
        this.nfv = nfv;
    }
    
    public ResponseEntity<String> getResult() {
        return result;
    }

    public void setResult(ResponseEntity<String> result) {
        this.result = result;
    }
}
