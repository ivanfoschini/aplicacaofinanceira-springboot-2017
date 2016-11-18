package aplicacaofinanceira.exception;

public class DifferentAccountsException extends Exception {
    
    public DifferentAccountsException(String mensagem) {
        super(mensagem);
    }
}