package aplicacaofinanceira.util;

import org.springframework.util.Base64Utils;

public class TestUtil {

    public static final String HTTP_NOT_READABLE_EXCEPTION = "org.springframework.http.converter.HttpMessageNotReadableException";
    public static final String NOT_EMPTY_COLLECTION_EXCEPTION = "aplicacaofinanceira.exception.NotEmptyCollectionException";
    public static final String NOT_FOUND_EXCEPTION = "aplicacaofinanceira.exception.NotFoundException";
    public static final String NOT_UNIQUE_EXCEPTION = "aplicacaofinanceira.exception.NotUniqueException";
    public static final String VALIDATION_EXCEPTION = "aplicacaofinanceira.exception.ValidationException";
    
    public static final String ID_COMPLEMENT_URI = "/{id}";
    
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
    
    public static String getClienteAuthorization() {
        return "Basic " + Base64Utils.encodeToString("cliente:cliente".getBytes());
    }
}