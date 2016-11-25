package aplicacaofinanceira.util;

import aplicacaofinanceira.model.Banco;
import org.springframework.util.Base64Utils;

public class TestUtil {

    public static final String BANCOS_URI = "/api/bancos";
    
    private static final String BANCO_CNPJ = "00000000000191";
    private static final String BANCO_NOME = "Banco do Brasil";
    private static final Integer BANCO_NUMERO = 1;
    
    public static Banco createBanco() {
        Banco banco = new Banco();
        banco.setNumero(BANCO_NUMERO);
        banco.setCnpj(BANCO_CNPJ);
        banco.setNome(BANCO_NOME);
        
        return banco;
    }
    
    public static String getAdminAuthorization() {
        return "Basic " + Base64Utils.encodeToString("admin:admin".getBytes());
    }
}
