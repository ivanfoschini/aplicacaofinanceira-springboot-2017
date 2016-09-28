package aplicacaofinanceira.controller;

import aplicacaofinanceira.exception.DefaultExceptionAttributes;
import aplicacaofinanceira.exception.ExceptionAttributes;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class BaseController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception exception, HttpServletRequest request) {
        logger.error("Exception start.");
        logger.error("Exception: ", exception);

        ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();

        Map<String, Object> responseBody = exceptionAttributes.getExceptionAttributes("JSON invalido", exception, request, HttpStatus.BAD_REQUEST);

        logger.error("Exception end.");
        
        return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.BAD_REQUEST);
    }
}
