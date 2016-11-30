package aplicacaofinanceira.util;

import aplicacaofinanceira.model.Estado;

public class EstadoTestUtil {
    
    public static final String ESTADOS_URI = "/api/estados";
    
    private static final String MINAS_GERAIS_NOME = "Minas Gerais";
    private static final String RIO_DE_JANEIRO_NOME = "Rio de Janeiro";
    private static final String SAO_PAULO_NOME = "Sao Paulo";
    
    public static Estado minasGerais() {
        Estado estado = new Estado();
        estado.setNome(MINAS_GERAIS_NOME);
        
        return estado;
    }
    
    public static Estado rioDeJaneiro() {
        Estado estado = new Estado();
        estado.setNome(RIO_DE_JANEIRO_NOME);
        
        return estado;
    }
    
    public static Estado saoPaulo() {
        Estado estado = new Estado();
        estado.setNome(SAO_PAULO_NOME);
        
        return estado;
    }
    
    public static Estado estadoComNomeComMaisDeDuzentosECinquentaECincoCaracteres() {
        Estado estado = new Estado();
        estado.setNome("123456789D123456789V123456789T123456789Q123456789C123456789S123456789S123456789O123456789N123456789C123456789D123456789V123456789T123456789Q123456789C123456789S123456789S123456789O123456789N123456789D123456789D123456789V123456789T123456789Q123456789C123456789S123456789S123456789O123456789N123456789C123456");
        
        return estado;
    }
    
    public static Estado estadoComNomeComMenosDeDoisCaracteres() {
        Estado estado = new Estado();
        estado.setNome("b");
        
        return estado;
    }
    
    public static Estado estadoSemCamposObrigatorios() {
        Estado estado = new Estado();
        estado.setNome(null);
        
        return estado;
    }
}