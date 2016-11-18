package aplicacaofinanceira.exception;

public class MoreThanOneAccountOwnershipException extends Exception {
    
    public MoreThanOneAccountOwnershipException(String mensagem) {
        super(mensagem);
    }
}