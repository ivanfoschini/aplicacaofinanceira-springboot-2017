package aplicacaofinanceira.util;

import aplicacaofinanceira.model.Agencia;
import aplicacaofinanceira.model.Endereco;

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
    
    public static Agencia agenciaComCepComMaisDeNoveCaracteres() {
        Agencia agencia = new Agencia();
        agencia.setNumero(NUMERO_DA_AGENCIA);
        agencia.setNome(NOME_DA_AGENCIA);
        
        Endereco endereco = new Endereco();
        endereco.setLogradouro(EnderecoTestUtil.LOGRADOURO);
        endereco.setNumero(EnderecoTestUtil.ENDERECO_NUMERO);
        endereco.setBairro(EnderecoTestUtil.BAIRRO);
        endereco.setCep(EnderecoTestUtil.CEP_WITH_MORE_THAN_NINE_CHARACTERS);
        
        agencia.setEndereco(endereco);
        
        return agencia;
    }
    
    public static Agencia agenciaComCepComMenosDeNoveCaracteres() {
        Agencia agencia = new Agencia();
        agencia.setNumero(NUMERO_DA_AGENCIA);
        agencia.setNome(NOME_DA_AGENCIA);
        
        Endereco endereco = new Endereco();
        endereco.setLogradouro(EnderecoTestUtil.LOGRADOURO);
        endereco.setNumero(EnderecoTestUtil.ENDERECO_NUMERO);
        endereco.setBairro(EnderecoTestUtil.BAIRRO);
        endereco.setCep(EnderecoTestUtil.CEP_WITH_LESS_THAN_NINE_CHARACTERS);
        
        agencia.setEndereco(endereco);
        
        return agencia;
    }
    
    public static Agencia agenciaComCepInvalido() {
        Agencia agencia = new Agencia();
        agencia.setNumero(NUMERO_DA_AGENCIA);
        agencia.setNome(NOME_DA_AGENCIA);
        
        Endereco endereco = new Endereco();
        endereco.setLogradouro(EnderecoTestUtil.LOGRADOURO);
        endereco.setNumero(EnderecoTestUtil.ENDERECO_NUMERO);
        endereco.setBairro(EnderecoTestUtil.BAIRRO);
        endereco.setCep(EnderecoTestUtil.CEP_INVALID);
        
        agencia.setEndereco(endereco);
        
        return agencia;
    }
    
    public static Agencia agenciaComNomeComMaisDeDuzentosECinquentaECincoCaracteres() {
        Agencia agencia = new Agencia();
        agencia.setNumero(NUMERO_DA_AGENCIA);
        agencia.setNome("123456789D123456789V123456789T123456789Q123456789C123456789S123456789S123456789O123456789N123456789C123456789D123456789V123456789T123456789Q123456789C123456789S123456789S123456789O123456789N123456789D123456789D123456789V123456789T123456789Q123456789C123456789S123456789S123456789O123456789N123456789C123456");
        
        return agencia;
    }
    
    public static Agencia agenciaComNomeComMenosDeDoisCaracteres() {
        Agencia agencia = new Agencia();
        agencia.setNumero(NUMERO_DA_AGENCIA);
        agencia.setNome("a");
        
        return agencia;
    }
    
    public static Agencia agenciaComNumeroMenorDoQueUm() {
        Agencia agencia = new Agencia();
        agencia.setNumero(0);
        agencia.setNome(NOME_DA_AGENCIA);
        
        return agencia;
    }
    
    public static Agencia agenciaSemCamposObrigatorios() {
        Agencia agencia = new Agencia();
        agencia.setNumero(0);
        agencia.setNome(null);
        
        Endereco endereco = new Endereco();
        endereco.setLogradouro(null);
        endereco.setNumero(0);
        endereco.setBairro(null);
        endereco.setCep(null);
        
        agencia.setEndereco(endereco);
        
        return agencia;
    }
}