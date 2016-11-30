package aplicacaofinanceira.util;

import aplicacaofinanceira.model.Cidade;

public class CidadeTestUtil {
    
    public static final String CIDADES_URI = "/api/cidades";

    private static final String CIDADE_NOME = "Sao Carlos";
    
    public static Cidade saoCarlos() {
        Cidade cidade = new Cidade();
        cidade.setNome(CIDADE_NOME);
        
        return cidade;
    }
    
    public static Cidade cidadeComNomeComMaisDeDuzentosECinquentaECincoCaracteres() {
        Cidade cidade = new Cidade();
        cidade.setNome("123456789D123456789V123456789T123456789Q123456789C123456789S123456789S123456789O123456789N123456789C123456789D123456789V123456789T123456789Q123456789C123456789S123456789S123456789O123456789N123456789D123456789D123456789V123456789T123456789Q123456789C123456789S123456789S123456789O123456789N123456789C123456");
        
        return cidade;
    }
    
    public static Cidade cidadeComNomeComMenosDeDoisCaracteres() {
        Cidade cidade = new Cidade();
        cidade.setNome("b");
        
        return cidade;
    }
    
    public static Cidade cidadeSemCamposObrigatorios() {
        Cidade cidade = new Cidade();
        cidade.setNome(null);
        
        return cidade;
    }
}