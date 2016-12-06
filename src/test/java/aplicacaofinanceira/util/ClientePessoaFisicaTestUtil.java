package aplicacaofinanceira.util;

import aplicacaofinanceira.model.ClientePessoaFisica;

public class ClientePessoaFisicaTestUtil {
 
    private static final String CLIENTE_PESSOA_FISICA_CPF = "12346549975";
    private static final String CLIENTE_PESSOA_FISICA_NOME = "Nome do Cliente Pessoa Fisica";
    private static final String CLIENTE_PESSOA_FISICA_RG = "111111111";
    private static final String CLIENTE_PESSOA_FISICA_STATUS = "ativo";
    
    public static ClientePessoaFisica clientePessoaFisica() {
        ClientePessoaFisica clientePessoaFisica = new ClientePessoaFisica();
        clientePessoaFisica.setNome(CLIENTE_PESSOA_FISICA_NOME);
        clientePessoaFisica.setStatus(CLIENTE_PESSOA_FISICA_STATUS);
        clientePessoaFisica.setRg(CLIENTE_PESSOA_FISICA_RG);
        clientePessoaFisica.setCpf(CLIENTE_PESSOA_FISICA_CPF);
        
        return clientePessoaFisica;
    }
}