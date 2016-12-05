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

    public int getContaNumero() {
        return contaNumero;
    }

    public String getContaDataDeAbertura() {
        return contaDataDeAbertura;
    }

    public float getContaSaldo() {
        return contaSaldo;
    }

    public float getContaLimite() {
        return contaLimite;
    }

    public Long getAgenciaId() {
        return agenciaId;
    }

    public String getAgenciaNome() {
        return agenciaNome;
    } 
}