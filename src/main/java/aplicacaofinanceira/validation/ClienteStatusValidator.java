package aplicacaofinanceira.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ClienteStatusValidator implements ConstraintValidator<ClienteStatus, String> {
    
    private static final String ATIVO = "ativo";
    private static final String INATIVO = "inativo";    
    
    @Override
    public void initialize(ClienteStatus constraintAnnotation) {}

    @Override
    public boolean isValid(String status, ConstraintValidatorContext context) {
        if (status == null || (!status.matches(ATIVO) && !status.matches(INATIVO))) {
            return false;
        }

        return true;
    }    
}