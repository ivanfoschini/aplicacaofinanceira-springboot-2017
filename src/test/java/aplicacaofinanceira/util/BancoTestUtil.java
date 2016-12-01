package aplicacaofinanceira.util;

import aplicacaofinanceira.model.Banco;

public class BancoTestUtil {

    public static final String BANCOS_URI = "/api/bancos";
    
    private static final String BANCO_DO_BRASIL_CNPJ = "00000000000191";
    private static final String BANCO_DO_BRASIL_NOME = "Banco do Brasil";
    private static final Integer BANCO_DO_BRASIL_NUMERO = 1;
    
    private static final String CAIXA_ECONOMICA_FEDERAL_CNPJ = "00360305000104";
    private static final String CAIXA_ECONOMICA_FEDERAL_NOME = "Caixa Economica Federal";
    private static final Integer CAIXA_ECONOMICA_FEDERAL_NUMERO = 2;
    
    private static final String ITAU_CNPJ = "60872504000123";
    private static final String ITAU_NOME = "Itau";
    private static final Integer ITAU_NUMERO = 3;
    
    public static Banco bancoDoBrasil() {
        Banco banco = new Banco();
        banco.setNumero(BANCO_DO_BRASIL_NUMERO);
        banco.setCnpj(BANCO_DO_BRASIL_CNPJ);
        banco.setNome(BANCO_DO_BRASIL_NOME);
        
        return banco;
    }
    
    public static Banco caixaEconomicaFederal() {
        Banco banco = new Banco();
        banco.setNumero(CAIXA_ECONOMICA_FEDERAL_NUMERO);
        banco.setCnpj(CAIXA_ECONOMICA_FEDERAL_CNPJ);
        banco.setNome(CAIXA_ECONOMICA_FEDERAL_NOME);
        
        return banco;
    }
    
    public static Banco itau() {
        Banco banco = new Banco();
        banco.setNumero(ITAU_NUMERO);
        banco.setCnpj(ITAU_CNPJ);
        banco.setNome(ITAU_NOME);
        
        return banco;
    }
    
    public static Banco bancoComCnpjInvalido() {
        Banco banco = new Banco();
        banco.setNumero(BANCO_DO_BRASIL_NUMERO);
        banco.setCnpj("11111111111111");
        banco.setNome(BANCO_DO_BRASIL_NOME);
        
        return banco;
    }
        
    public static Banco bancoComNomeComMaisDeDuzentosECinquentaECincoCaracteres() {
        Banco banco = new Banco();
        banco.setNumero(BANCO_DO_BRASIL_NUMERO);
        banco.setCnpj(BANCO_DO_BRASIL_CNPJ);
        banco.setNome("123456789D123456789V123456789T123456789Q123456789C123456789S123456789S123456789O123456789N123456789C123456789D123456789V123456789T123456789Q123456789C123456789S123456789S123456789O123456789N123456789D123456789D123456789V123456789T123456789Q123456789C123456789S123456789S123456789O123456789N123456789C123456");
        
        return banco;
    }
    
    public static Banco bancoComNomeComMenosDeDoisCaracteres() {
        Banco banco = new Banco();
        banco.setNumero(BANCO_DO_BRASIL_NUMERO);
        banco.setCnpj(BANCO_DO_BRASIL_CNPJ);
        banco.setNome("b");
        
        return banco;
    }
    
    public static Banco bancoComNumeroMenorDoQueUm() {
        Banco banco = new Banco();
        banco.setNumero(0);
        banco.setCnpj(BANCO_DO_BRASIL_CNPJ);
        banco.setNome(BANCO_DO_BRASIL_NOME);
        
        return banco;
    }
    
    public static Banco bancoSemCamposObrigatorios() {
        Banco banco = new Banco();
        banco.setNumero(0);
        banco.setCnpj(null);
        banco.setNome(null);
        
        return banco;
    }
}