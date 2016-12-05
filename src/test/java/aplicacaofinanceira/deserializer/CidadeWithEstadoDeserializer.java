package aplicacaofinanceira.deserializer;

public class CidadeWithEstadoDeserializer {
    
    private Long cidadeId;
    private String cidadeNome;
    private Long estadoId;
    private String estadoNome;

    public CidadeWithEstadoDeserializer() {}

    public Long getCidadeId() {
        return cidadeId;
    }

    public String getCidadeNome() {
        return cidadeNome;
    }

    public Long getEstadoId() {
        return estadoId;
    }

    public String getEstadoNome() {
        return estadoNome;
    }       
}