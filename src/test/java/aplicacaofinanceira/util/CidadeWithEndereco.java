package aplicacaofinanceira.util;

import aplicacaofinanceira.model.Cidade;
import aplicacaofinanceira.model.Endereco;
import aplicacaofinanceira.model.Estado;

public class CidadeWithEndereco {
    
    private Cidade cidade;
    private Endereco endereco;
    private Estado estado;

    public CidadeWithEndereco(Cidade cidade, Endereco endereco, Estado estado) {
        this.cidade = cidade;
        this.endereco = endereco;
        this.estado = estado;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}