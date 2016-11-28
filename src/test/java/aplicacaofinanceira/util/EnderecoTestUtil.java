package aplicacaofinanceira.util;

import aplicacaofinanceira.model.Endereco;

public class EnderecoTestUtil {
    
    private static final String BAIRRO = "Bairro";
    private static final String CEP = "11111-111";
    private static final String COMPLEMENTO = "Complemento";
    private static final String LOGRADOURO = "Logradouro";
    
    private static final Integer ENDERECO_NUMERO = 1;
    
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
