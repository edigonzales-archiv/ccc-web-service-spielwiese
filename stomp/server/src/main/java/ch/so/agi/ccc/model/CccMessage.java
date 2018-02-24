package ch.so.agi.ccc.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CccMessage {
    
    private String apiVersion;
    
    private String sessionId;
    
    private SenderType sender;
    
    private Boolean ready;
    
    private String context;
    
    private String method;
    
    public enum SenderType {
        APP,
        GIS,
        SERVER
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public SenderType getSender() {
        return sender;
    }

    public void setSender(SenderType sender) {
        this.sender = sender;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Boolean isReady() {
        return ready;
    }

    public void setReady(Boolean ready) {
        this.ready = ready;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "CccMessage [apiVersion=" + apiVersion + ", sessionId=" + sessionId + ", sender=" + sender + ", ready="
                + ready + ", context=" + context + ", method=" + method + "]";
    }
}
