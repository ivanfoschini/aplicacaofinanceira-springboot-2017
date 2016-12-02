package aplicacaofinanceira.deserializer;

public class ContaCorrenteWithAgenciaDeserializer {
 
    private Long contaId;
    private int contaNumero;
    private String contaDataDeAbertura;
    private float contaSaldo;
    private float contaLimite;
    private Long agenciaId;
    private String agenciaNome;

    public ContaCorrenteWithAgenciaDeserializer() {}

    public Long getContaId() {
        return contaId;
    }

    public void setContaId(Long contaId) {
        this.contaId = contaId;
    }

    public int getContaNumero() {
        return contaNumero;
    }

    public void setContaNumero(int contaNumero) {
        this.contaNumero = contaNumero;
    }

    public String getContaDataDeAbertura() {
        return contaDataDeAbertura;
    }

    public void setContaDataDeAbertura(String contaDataDeAbertura) {
        this.contaDataDeAbertura = contaDataDeAbertura;
    }

    public float getContaSaldo() {
        return contaSaldo;
    }

    public void setContaSaldo(float contaSaldo) {
        this.contaSaldo = contaSaldo;
    }

    public float getContaLimite() {
        return contaLimite;
    }

    public void setContaLimite(float contaLimite) {
        this.contaLimite = contaLimite;
    }

    public Long getAgenciaId() {
        return agenciaId;
    }

    public void setAgenciaId(Long agenciaId) {
        this.agenciaId = agenciaId;
    }

    public String getAgenciaNome() {
        return agenciaNome;
    }

    public void setAgenciaNome(String agenciaNome) {
        this.agenciaNome = agenciaNome;
    }    
}