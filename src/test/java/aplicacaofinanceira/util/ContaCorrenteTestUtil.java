package aplicacaofinanceira.util;

import aplicacaofinanceira.model.ContaCorrente;
import org.joda.time.LocalDate;

public class ContaCorrenteTestUtil {
    
    public static final String CONTAS_CORRENTES_URI = "/api/contasCorrentes";
    
    public final static String CONTA_CORRENTE_DATA_DE_ABERTURA = "2017-01-01";   
    public final static float CONTA_CORRENTE_LIMITE = 100.0f;   
    public final static int CONTA_CORRENTE_NUMERO = 1;
    public final static float CONTA_CORRENTE_SALDO = 10.0f;       
    
    public static ContaCorrente contaCorrente() {
        ContaCorrente contaCorrente = new ContaCorrente();
        contaCorrente.setNumero(CONTA_CORRENTE_NUMERO);
        contaCorrente.setSaldo(CONTA_CORRENTE_SALDO);
        contaCorrente.setDataDeAbertura(new LocalDate());
        contaCorrente.setLimite(CONTA_CORRENTE_LIMITE);
        
        return contaCorrente;
    }
}
