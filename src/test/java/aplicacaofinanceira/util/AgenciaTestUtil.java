package aplicacaofinanceira.util;

import aplicacaofinanceira.model.Agencia;

public class AgenciaTestUtil {
    
    public static final String AGENCIAS_URI = "/api/agencias";

    private static final String NOME_DA_AGENCIA = "Nome da Agencia";
    
    private static final Integer NUMERO_DA_AGENCIA = 1;
    
    public static Agencia agencia() {
        Agencia agencia = new Agencia();
        agencia.setNumero(NUMERO_DA_AGENCIA);
        agencia.setNome(NOME_DA_AGENCIA);
        
        return agencia;
    }
}
