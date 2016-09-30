package aplicacaofinanceira.validation;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ValidationUtil {
    private static final String KEY_ERROR = "error";
    
    public static String getBeanValidationErrors(BindingResult bindingResult) {
        JSONArray errorsJSONArray = new JSONArray();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();            

        for (FieldError error: fieldErrors) {
            JSONObject errorJSONObject = new JSONObject();
            errorJSONObject.put(KEY_ERROR, error.getDefaultMessage());

            errorsJSONArray.put(errorJSONObject);
        }
        
        return errorsJSONArray.toString();
    }
}
