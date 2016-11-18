package aplicacaofinanceira.exception;

public class NoAccountOwnershipException extends Exception {
    
    public NoAccountOwnershipException(String mensagem) {
        super(mensagem);
    }    
}
