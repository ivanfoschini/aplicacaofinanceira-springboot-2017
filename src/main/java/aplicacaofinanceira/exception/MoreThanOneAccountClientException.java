package aplicacaofinanceira.exception;

public class MoreThanOneAccountClientException extends Exception {
    
    public MoreThanOneAccountClientException(String mensagem) {
        super(mensagem);
    }    
}