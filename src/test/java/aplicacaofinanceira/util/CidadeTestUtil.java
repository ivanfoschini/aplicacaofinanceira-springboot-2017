package aplicacaofinanceira.util;

import aplicacaofinanceira.model.Cidade;

public class CidadeTestUtil {
    
    public static final String CIDADES_URI = "/api/cidades";

    private static final String ARACATUBA_CIDADE_NOME = "Aracatuba";
    private static final String RIO_DE_JANEIRO_CIDADE_NOME = "Rio de Janeiro";
    private static final String SAO_CARLOS_CIDADE_NOME = "Sao Carlos";
    private static final String UBERLANDIA_CIDADE_NOME = "Uberlandia";
    private static final String VOTUPORANGA_CIDADE_NOME = "Votuporanga";
    
    public static Cidade aracatuba() {
        Cidade cidade = new Cidade();
        cidade.setNome(ARACATUBA_CIDADE_NOME);
        
        return cidade;
    }
    
    public static Cidade rioDeJaneiro() {
        Cidade cidade = new Cidade();
        cidade.setNome(RIO_DE_JANEIRO_CIDADE_NOME);
        
        return cidade;
    }
    
    public static Cidade saoCarlos() {
        Cidade cidade = new Cidade();
        cidade.setNome(SAO_CARLOS_CIDADE_NOME);
        
        return cidade;
    }
    
    public static Cidade uberlandia() {
        Cidade cidade = new Cidade();
        cidade.setNome(UBERLANDIA_CIDADE_NOME);
        
        return cidade;
    }
    
    public static Cidade votuporanga() {
        Cidade cidade = new Cidade();
        cidade.setNome(VOTUPORANGA_CIDADE_NOME);
        
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