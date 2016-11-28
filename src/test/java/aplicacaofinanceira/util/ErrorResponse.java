package aplicacaofinanceira.util;

import java.util.LinkedList;
import java.util.List;

public class ErrorResponse {
    
    private String timestamp;
    private int status;
    private String reason;
    private String exception;
    private String message;
    private List<String> messages = new LinkedList<>();    
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