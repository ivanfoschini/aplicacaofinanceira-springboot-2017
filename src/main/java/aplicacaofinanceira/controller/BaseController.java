package aplicacaofinanceira.controller;

import aplicacaofinanceira.exception.DefaultExceptionAttributes;
import aplicacaofinanceira.exception.ExceptionAttributes;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class BaseController {

    @Autowired
    private MessageSource messageSource;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(Exception exception, HttpServletRequest request) {
        logExceptionStart(exception);

        ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();

        Map<String, Object> responseBody = exceptionAttributes.getExceptionAttributes(messageSource.getMessage("bancoUniqueError", null, null), exception, request, HttpStatus.BAD_REQUEST);

        logExceptionEnd();

        return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleEmptyResultDataAccessException(Exception exception, HttpServletRequest request) {
        logExceptionStart(exception);
        
        ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();

        Map<String, Object> responseBody = exceptionAttributes.getExceptionAttributes(messageSource.getMessage("bancoNotFoundError", null, null), exception, request, HttpStatus.BAD_REQUEST);

        logExceptionEnd();

        return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(Exception exception, HttpServletRequest request) {
        logExceptionStart(exception);
        
        ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();

        Map<String, Object> responseBody = exceptionAttributes.getExceptionAttributes(messageSource.getMessage("generalBadRequest", null, null), exception, request, HttpStatus.BAD_REQUEST);

        logExceptionEnd();
        
        return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.BAD_REQUEST);
    }
    
    protected void logExceptionStart(Exception exception) {
        logger.error("====================");
        logger.error("Exception start.");
        logger.error("Exception: ", exception);        
    }
    
    protected void logExceptionEnd() {
        logger.error("Exception end.");
        logger.error("====================");
    }
}
