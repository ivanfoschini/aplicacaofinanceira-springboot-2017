package aplicacaofinanceira.controller;

import aplicacaofinanceira.exception.CampoUniqueException;
import aplicacaofinanceira.exception.DefaultExceptionAttributes;
import aplicacaofinanceira.exception.ExceptionAttributes;
import aplicacaofinanceira.exception.NotFoundException;
import java.util.Map;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class BaseController {

    @Autowired
    private MessageSource messageSource;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(CampoUniqueException.class)
    public ResponseEntity<Map<String, Object>> handleCampoUniqueException(Exception exception, HttpServletRequest request) {
        logExceptionStart(exception);

        ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();

        Map<String, Object> responseBody = exceptionAttributes.getExceptionAttributes(exception.getMessage(), exception, request, HttpStatus.BAD_REQUEST);

        logExceptionEnd();

        return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<Map<String, Object>> handleNoResultException(Exception exception, HttpServletRequest request) {
        logExceptionStart(exception);

        ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();

        Map<String, Object> responseBody = exceptionAttributes.getExceptionAttributes(exception.getMessage(), exception, request, HttpStatus.BAD_REQUEST);

        logExceptionEnd();

        return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(Exception exception, HttpServletRequest request) {
        logExceptionStart(exception);

        ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();

        Map<String, Object> responseBody = exceptionAttributes.getExceptionAttributes(exception.getMessage(), exception, request, HttpStatus.BAD_REQUEST);

        logExceptionEnd();

        return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(Exception exception, HttpServletRequest request) {
        logExceptionStart(exception);

        ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();

        Map<String, Object> responseBody = exceptionAttributes.getExceptionAttributes(messageSource.getMessage("generalBadRequest", null, null), exception, request, HttpStatus.BAD_REQUEST);

        logExceptionEnd();

        return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception exception, HttpServletRequest request) {
        logExceptionStart(exception);

        ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();

        Map<String, Object> responseBody = exceptionAttributes.getExceptionAttributes(messageSource.getMessage("generalInternalServerError", null, null), exception, request, HttpStatus.INTERNAL_SERVER_ERROR);

        logExceptionEnd();
        
        return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
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
