package aplicacaofinanceira.util;

import aplicacaofinanceira.model.Cidade;
import aplicacaofinanceira.model.Estado;

public class EstadoWithCidade {
    
    private Cidade cidade;
    private Estado estado;

    public EstadoWithCidade(Cidade cidade, Estado estado) {
        this.cidade = cidade;
        this.estado = estado;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}