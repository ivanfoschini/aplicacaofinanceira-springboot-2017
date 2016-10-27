package aplicacaofinanceira.exception;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

public class DefaultExceptionAttributes implements ExceptionAttributes {

    private static final String EXCEPTION = "exception";
    private static final String MESSAGE = "message";
    private static final String MESSAGES = "messages";
    private static final String PATH = "path";
    private static final String REASON = "reason";        
    private static final String STATUS = "status";
    private static final String TIMESTAMP = "timestamp";

    @Override
    public Map<String, Object> getExceptionAttributes(String message, Exception exception, HttpServletRequest httpRequest, HttpStatus httpStatus) {
        Map<String, Object> exceptionAttributes = new LinkedHashMap<>();

        addTimestamp(exceptionAttributes);
        addHttpStatus(exceptionAttributes, httpStatus);
        addExceptionDetail(exceptionAttributes, message, exception);
        addPath(exceptionAttributes, httpRequest);

        return exceptionAttributes;
    }
    
    @Override
    public Map<String, Object> getExceptionAttributes(List messages, Exception exception, HttpServletRequest httpRequest, HttpStatus httpStatus) {
        Map<String, Object> exceptionAttributes = new LinkedHashMap<>();

        addTimestamp(exceptionAttributes);
        addHttpStatus(exceptionAttributes, httpStatus);
        addExceptionDetail(exceptionAttributes, messages, exception);
        addPath(exceptionAttributes, httpRequest);

        return exceptionAttributes;
    }

    private void addPath(Map<String, Object> exceptionAttributes, HttpServletRequest httpRequest) {
        exceptionAttributes.put(PATH, httpRequest.getServletPath());
    }
    
    private void addExceptionDetail(Map<String, Object> exceptionAttributes, String message, Exception exception) {
        exceptionAttributes.put(EXCEPTION, exception.getClass().getName());
        exceptionAttributes.put(MESSAGE, message);
    }

    private void addExceptionDetail(Map<String, Object> exceptionAttributes, List messages, Exception exception) {
        exceptionAttributes.put(EXCEPTION, exception.getClass().getName());
        exceptionAttributes.put(MESSAGES, messages);
    }
    
    private void addHttpStatus(Map<String, Object> exceptionAttributes, HttpStatus httpStatus) {
        exceptionAttributes.put(STATUS, httpStatus.value());
        exceptionAttributes.put(REASON, httpStatus.getReasonPhrase());
    }

    private void addTimestamp(Map<String, Object> exceptionAttributes) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        exceptionAttributes.put(TIMESTAMP, formatter.format(new Date()));
    }       
}
