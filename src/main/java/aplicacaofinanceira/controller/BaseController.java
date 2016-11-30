package aplicacaofinanceira.controller;

import aplicacaofinanceira.exception.DefaultExceptionAttributes;
import aplicacaofinanceira.exception.ExceptionAttributes;
import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.NotUniqueException;
import aplicacaofinanceira.exception.ValidationException;
import aplicacaofinanceira.validation.ValidationUtil;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class BaseController {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception exception, HttpServletRequest request) {
        ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();

        Map<String, Object> responseBody = exceptionAttributes.getExceptionAttributes(messageSource.getMessage("generalInternalServerError", null, null), exception, request, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(Exception exception, HttpServletRequest request) {
        ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();

        Map<String, Object> responseBody = exceptionAttributes.getExceptionAttributes(exception.getMessage(), exception, request, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }    
    
    @ExceptionHandler(NotUniqueException.class)
    public ResponseEntity<Map<String, Object>> handleCampoUniqueException(Exception exception, HttpServletRequest request) {
        ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();

        Map<String, Object> responseBody = exceptionAttributes.getExceptionAttributes(exception.getMessage(), exception, request, HttpStatus.UNPROCESSABLE_ENTITY);

        return new ResponseEntity<>(responseBody, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(Exception exception, HttpServletRequest request) {
        ExceptionAttributes exceptionAttributes = new DefaultExceptionAttributes();

        Map<String, Object> responseBody = exceptionAttributes.getExceptionAttributes(ValidationUtil.errorsList, exception, request, HttpStatus.UNPROCESSABLE_ENTITY);

        return new ResponseEntity<>(responseBody, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
