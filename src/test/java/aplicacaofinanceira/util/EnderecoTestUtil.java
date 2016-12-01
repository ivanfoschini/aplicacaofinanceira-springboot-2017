package aplicacaofinanceira.util;

import aplicacaofinanceira.model.Endereco;

public class EnderecoTestUtil {
    
    public static final String BAIRRO = "Bairro";
    public static final String CEP = "11111-111";
    public static final String CEP_INVALID = "11111-11X";
    public static final String CEP_WITH_LESS_THAN_NINE_CHARACTERS = "11111-11";
    public static final String CEP_WITH_MORE_THAN_NINE_CHARACTERS = "11111-1111";
    public static final String COMPLEMENTO = "Complemento";
    public static final String LOGRADOURO = "Logradouro";
    
    public static final Integer ENDERECO_NUMERO = 1;
    
    public static Endereco validEndereco() {
        Endereco endereco = new Endereco();
        endereco.setLogradouro(LOGRADOURO);
        endereco.setNumero(ENDERECO_NUMERO);
        endereco.setComplemento(COMPLEMENTO);        
        endereco.setBairro(BAIRRO);
        endereco.setCep(CEP);
        
        return endereco;
    }    
}
