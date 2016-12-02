package aplicacaofinanceira.deserializer;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponseDeserializer {
    
    private String timestamp;
    private int status;
    private String reason;
    private String exception;
    private String message;
    private List<String> messages = new ArrayList<>();    
    private String path;

    public String getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public String getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getMessages() {
        return messages;
    }

    public String getPath() {
        return path;
    }        
}