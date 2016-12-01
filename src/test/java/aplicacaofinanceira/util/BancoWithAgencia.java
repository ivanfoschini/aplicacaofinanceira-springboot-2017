package aplicacaofinanceira.util;

import aplicacaofinanceira.model.Agencia;
import aplicacaofinanceira.model.Banco;
import aplicacaofinanceira.model.Cidade;
import aplicacaofinanceira.model.Estado;

public class BancoWithAgencia {
    
    private Agencia agencia;
    private Banco banco;
    private Cidade cidade;
    private Estado estado;

    public BancoWithAgencia(Agencia agencia, Banco banco, Cidade cidade, Estado estado) {
        this.agencia = agencia;
        this.banco = banco;
        this.cidade = cidade;
        this.estado = estado;
    }

    public Agencia getAgencia() {
        return agencia;
    }

    public void setAgencia(Agencia agencia) {
        this.agencia = agencia;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
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