package aplicacaofinanceira.validation;

import aplicacaofinanceira.exception.ValidationException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ValidationUtil {
    public static List<String> errorsList;         
    
    public static List handleValidationErrors(BindingResult bindingResult) throws ValidationException {
        errorsList = new ArrayList();
        
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();            

        for (FieldError error: fieldErrors) {
            errorsList.add(error.getDefaultMessage());
        }
        
        throw new ValidationException();
    }
}
