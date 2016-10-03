package aplicacaofinanceira.exception;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

public class DefaultExceptionAttributes implements ExceptionAttributes {

    public static final String EXCEPTION = "exception";
    public static final String MESSAGE = "message";
    public static final String PATH = "path";
    public static final String REASON = "reason";        
    public static final String STATUS = "status";
    public static final String TIMESTAMP = "timestamp";

    @Override
    public Map<String, Object> getExceptionAttributes(String message, Exception exception, HttpServletRequest httpRequest, HttpStatus httpStatus) {
        Map<String, Object> exceptionAttributes = new LinkedHashMap<String, Object>();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        exceptionAttributes.put(TIMESTAMP, formatter.format(new Date()));
        addHttpStatus(exceptionAttributes, httpStatus);
        addExceptionDetail(exceptionAttributes, message, exception);
        addPath(exceptionAttributes, httpRequest);

        return exceptionAttributes;
    }

    private void addHttpStatus(Map<String, Object> exceptionAttributes, HttpStatus httpStatus) {
        exceptionAttributes.put(STATUS, httpStatus.value());
        exceptionAttributes.put(REASON, httpStatus.getReasonPhrase());
    }

    private void addExceptionDetail(Map<String, Object> exceptionAttributes, String message, Exception exception) {
        exceptionAttributes.put(EXCEPTION, exception.getClass().getName());
        exceptionAttributes.put(MESSAGE, message);
    }

    private void addPath(Map<String, Object> exceptionAttributes, HttpServletRequest httpRequest) {
        exceptionAttributes.put(PATH, httpRequest.getServletPath());
    }    
}
