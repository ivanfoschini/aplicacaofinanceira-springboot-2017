package aplicacaofinanceira.deserializer;

public class AgenciaWithEnderecoAndBancoDeserializer {
 
    private Long agenciaId;
    private String agenciaNome;
    private int agenciaNumero;
    private Long enderecoId;
    private String enderecoLogradouro;
    private int enderecoNumero;
    private String enderecoComplemento;
    private String enderecoBairro;
    private String enderecoCep;
    private Long cidadeId;
    private String cidadeNome;
    private Long estadoId;
    private String estadoNome;
    private Long bancoId;
    private String bancoNome;

    public AgenciaWithEnderecoAndBancoDeserializer() {}

    public Long getAgenciaId() {
        return agenciaId;
    }

    public String getAgenciaNome() {
        return agenciaNome;
    }

    public int getAgenciaNumero() {
        return agenciaNumero;
    }

    public Long getEnderecoId() {
        return enderecoId;
    }

    public String getEnderecoLogradouro() {
        return enderecoLogradouro;
    }

    public int getEnderecoNumero() {
        return enderecoNumero;
    }

    public String getEnderecoComplemento() {
        return enderecoComplemento;
    }

    public String getEnderecoBairro() {
        return enderecoBairro;
    }

    public String getEnderecoCep() {
        return enderecoCep;
    }

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

    public Long getBancoId() {
        return bancoId;
    }

    public String getBancoNome() {
        return bancoNome;
    }       
}