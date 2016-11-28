package aplicacaofinanceira.util;

import aplicacaofinanceira.model.Cidade;

public class CidadeTestUtil {

    private static final String CIDADE_NOME = "Sao Carlos";
    
    public static Cidade saoCarlos() {
        Cidade cidade = new Cidade();
        cidade.setNome(CIDADE_NOME);
        
        return cidade;
    }
}
