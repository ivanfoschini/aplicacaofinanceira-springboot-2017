package aplicacaofinanceira.util;

import aplicacaofinanceira.model.ContaCorrente;
import org.joda.time.LocalDate;

public class ContaCorrenteUtil {

    private final static float CONTA_CORRENTE_LIMITE = 100.0f;   
    private final static int CONTA_CORRENTE_NUMERO = 1;
    private final static float CONTA_CORRENTE_SALDO = 10.0f;       
    
    public static ContaCorrente contaCorrente() {
        ContaCorrente contaCorrente = new ContaCorrente();
        contaCorrente.setNumero(CONTA_CORRENTE_NUMERO);
        contaCorrente.setSaldo(CONTA_CORRENTE_SALDO);
        contaCorrente.setDataDeAbertura(new LocalDate());
        contaCorrente.setLimite(CONTA_CORRENTE_LIMITE);
        
        return contaCorrente;
    }
}
