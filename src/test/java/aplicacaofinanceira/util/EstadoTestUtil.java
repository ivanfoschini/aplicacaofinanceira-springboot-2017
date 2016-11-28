package aplicacaofinanceira.util;

import aplicacaofinanceira.model.Estado;

public class EstadoTestUtil {
    
    private static final String ESTADO_NOME = "Sao Paulo";
    
    public static Estado saoPaulo() {
        Estado estado = new Estado();
        estado.setNome(ESTADO_NOME);
        
        return estado;
    }
}