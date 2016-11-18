package aplicacaofinanceira.util;

public class CorrentistaJson {
    
    private boolean titularidade;
    private Long contaId;
    private Long clienteId;

    public CorrentistaJson() {}

    public boolean isTitularidade() {
        return titularidade;
    }

    public void setTitularidade(boolean titularidade) {
        this.titularidade = titularidade;
    }

    public Long getContaId() {
        return contaId;
    }

    public void setContaId(Long contaId) {
        this.contaId = contaId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
}