package aplicacaofinanceira.util;

import org.springframework.util.Base64Utils;

public class TestUtil {

    public final static String NOT_FOUND_EXCEPTION = "aplicacaofinanceira.exception.NotFoundException";
    public final static String NOT_UNIQUE_EXCEPTION = "aplicacaofinanceira.exception.NotUniqueException";
    public final static String VALIDATION_EXCEPTION = "aplicacaofinanceira.exception.ValidationException";
    
    public final static int DEFAULT_SUCCESS_LIST_SIZE = 3;

    public static String getAdminAuthorization() {
        return "Basic " + Base64Utils.encodeToString("admin:admin".getBytes());
    }
    
    public static String getAdminAuthorizationWithWrongPassword() {
        return "Basic " + Base64Utils.encodeToString("admin:adminadmin".getBytes());
    }
    
    public static String getFuncionarioAuthorization() {
        return "Basic " + Base64Utils.encodeToString("funcionario:funcionario".getBytes());
    }
}