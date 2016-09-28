package aplicacaofinanceira.exception;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

public interface ExceptionAttributes {
    
    Map<String, Object> getExceptionAttributes(String message, Exception exception, HttpServletRequest httpRequest, HttpStatus httpStatus);
}
