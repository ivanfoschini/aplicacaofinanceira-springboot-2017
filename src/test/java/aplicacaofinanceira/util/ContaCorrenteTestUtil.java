package aplicacaofinanceira.util;

import aplicacaofinanceira.model.ContaCorrente;
import org.joda.time.LocalDate;

public class ContaCorrenteTestUtil {
    
    public static final String CONTAS_CORRENTES_URI = "/api/contasCorrentes";
    
    public final static String CONTA_CORRENTE_DATA_DE_ABERTURA_UM = "2017-01-01";   
    public final static float CONTA_CORRENTE_LIMITE_UM = 100.0f;   
    public final static int CONTA_CORRENTE_NUMERO_UM = 1;
    public final static float CONTA_CORRENTE_SALDO_UM = 10.0f;       
    
    public final static String CONTA_CORRENTE_DATA_DE_ABERTURA_DOIS = "2017-02-02";   
    public final static float CONTA_CORRENTE_LIMITE_DOIS = 200.0f;   
    public final static int CONTA_CORRENTE_NUMERO_DOIS = 2;
    public final static float CONTA_CORRENTE_SALDO_DOIS = 20.0f;       
    
    public final static String CONTA_CORRENTE_DATA_DE_ABERTURA_TRES = "2017-03-03";   
    public final static float CONTA_CORRENTE_LIMITE_TRES = 300.0f;   
    public final static int CONTA_CORRENTE_NUMERO_TRES = 3;
    public final static float CONTA_CORRENTE_SALDO_TRES = 30.0f;       
    
    public static ContaCorrente contaCorrenteUm() {
        ContaCorrente contaCorrente = new ContaCorrente();
        contaCorrente.setNumero(CONTA_CORRENTE_NUMERO_UM);
        contaCorrente.setSaldo(CONTA_CORRENTE_SALDO_UM);
        contaCorrente.setDataDeAbertura(new LocalDate());
        contaCorrente.setLimite(CONTA_CORRENTE_LIMITE_UM);
        
        return contaCorrente;
    }
    
    public static ContaCorrente contaCorrenteDois() {
        ContaCorrente contaCorrente = new ContaCorrente();
        contaCorrente.setNumero(CONTA_CORRENTE_NUMERO_DOIS);
        contaCorrente.setSaldo(CONTA_CORRENTE_SALDO_DOIS);
        contaCorrente.setDataDeAbertura(new LocalDate());
        contaCorrente.setLimite(CONTA_CORRENTE_LIMITE_DOIS);
        
        return contaCorrente;
    }
    
    public static ContaCorrente contaCorrenteTres() {
        ContaCorrente contaCorrente = new ContaCorrente();
        contaCorrente.setNumero(CONTA_CORRENTE_NUMERO_TRES);
        contaCorrente.setSaldo(CONTA_CORRENTE_SALDO_TRES);
        contaCorrente.setDataDeAbertura(new LocalDate());
        contaCorrente.setLimite(CONTA_CORRENTE_LIMITE_TRES);
        
        return contaCorrente;
    }
}
