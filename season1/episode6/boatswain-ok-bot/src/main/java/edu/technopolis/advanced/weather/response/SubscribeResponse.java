package edu.technopolis.advanced.weather.response;

public class SubscribeResponse extends Response {
    boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
