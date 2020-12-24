package it.polito.astrid.models;

public class InterceptionRequest {
    String userId;
    String providerId;
    String serviceId;
    String command;

    public InterceptionRequest(String userId, String providerId, String serviceId, String command) {
        this.userId = userId;
        this.providerId = providerId;
        this.serviceId = serviceId;
        this.command = command;
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
}
