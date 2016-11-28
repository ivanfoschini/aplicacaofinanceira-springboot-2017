package aplicacaofinanceira.util;

import aplicacaofinanceira.model.Banco;

public class BancoTestUtil {

    private static final String BANCO_CNPJ = "00000000000191";
    private static final String BANCO_NOME = "Banco do Brasil";
    private static final Integer BANCO_NUMERO = 1;
    
    public static final String BANCOS_URI = "/api/bancos";
    
    public static final int SAVE_SEM_CAMPOS_OBRIGATORIOS_MESSAGE_LIST_ERRORS_SIZE = 4;
    
    public static Banco bancoComCnpjInvalido() {
        Banco banco = new Banco();
        banco.setNumero(BANCO_NUMERO);
        banco.setCnpj("11111111111111");
        banco.setNome(BANCO_NOME);
        
        return banco;
    }
        
    public static Banco bancoComNomeComMaisDeDuzentosECinquentaECincoCaracteres() {
        Banco banco = new Banco();
        banco.setNumero(BANCO_NUMERO);
        banco.setCnpj(BANCO_CNPJ);
        banco.setNome("123456789D123456789V123456789T123456789Q123456789C123456789S123456789S123456789O123456789N123456789C123456789D123456789V123456789T123456789Q123456789C123456789S123456789S123456789O123456789N123456789D123456789D123456789V123456789T123456789Q123456789C123456789S123456789S123456789O123456789N123456789C123456");
        
        return banco;
    }
    
    public static Banco bancoComNomeComMenosDeDoisCaracteres() {
        Banco banco = new Banco();
        banco.setNumero(BANCO_NUMERO);
        banco.setCnpj(BANCO_CNPJ);
        banco.setNome("b");
        
        return banco;
    }
    
    public static Banco bancoComNumeroMenorDoQueUm() {
        Banco banco = new Banco();
        banco.setNumero(0);
        banco.setCnpj(BANCO_CNPJ);
        banco.setNome(BANCO_NOME);
        
        return banco;
    }
    
    public static Banco bancoSemCamposObrigatorios() {
        Banco banco = new Banco();
        banco.setNumero(null);
        banco.setCnpj(null);
        banco.setNome(null);
        
        return banco;
    }
    
    public static Banco bancoValido() {
        Banco banco = new Banco();
        banco.setNumero(BANCO_NUMERO);
        banco.setCnpj(BANCO_CNPJ);
        banco.setNome(BANCO_NOME);
        
        return banco;
    }
}