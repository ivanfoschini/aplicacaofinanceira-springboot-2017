package aplicacaofinanceira.exception;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

public interface ExceptionAttributes {
    
    Map<String, Object> getExceptionAttributes(String message, Exception exception, HttpServletRequest httpRequest, HttpStatus httpStatus);
    
    Map<String, Object> getExceptionAttributes(List messages, Exception exception, HttpServletRequest httpRequest, HttpStatus httpStatus);
}
