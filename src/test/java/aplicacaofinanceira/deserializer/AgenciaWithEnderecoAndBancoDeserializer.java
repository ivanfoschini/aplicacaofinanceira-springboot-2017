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

    public void setAgenciaId(Long agenciaId) {
        this.agenciaId = agenciaId;
    }

    public String getAgenciaNome() {
        return agenciaNome;
    }

    public void setAgenciaNome(String agenciaNome) {
        this.agenciaNome = agenciaNome;
    }

    public int getAgenciaNumero() {
        return agenciaNumero;
    }

    public void setAgenciaNumero(int agenciaNumero) {
        this.agenciaNumero = agenciaNumero;
    }

    public Long getEnderecoId() {
        return enderecoId;
    }

    public void setEnderecoId(Long enderecoId) {
        this.enderecoId = enderecoId;
    }

    public String getEnderecoLogradouro() {
        return enderecoLogradouro;
    }

    public void setEnderecoLogradouro(String enderecoLogradouro) {
        this.enderecoLogradouro = enderecoLogradouro;
    }

    public int getEnderecoNumero() {
        return enderecoNumero;
    }

    public void setEnderecoNumero(int enderecoNumero) {
        this.enderecoNumero = enderecoNumero;
    }

    public String getEnderecoComplemento() {
        return enderecoComplemento;
    }

    public void setEnderecoComplemento(String enderecoComplemento) {
        this.enderecoComplemento = enderecoComplemento;
    }

    public String getEnderecoBairro() {
        return enderecoBairro;
    }

    public void setEnderecoBairro(String enderecoBairro) {
        this.enderecoBairro = enderecoBairro;
    }

    public String getEnderecoCep() {
        return enderecoCep;
    }

    public void setEnderecoCep(String enderecoCep) {
        this.enderecoCep = enderecoCep;
    }

    public Long getCidadeId() {
        return cidadeId;
    }

    public void setCidadeId(Long cidadeId) {
        this.cidadeId = cidadeId;
    }

    public String getCidadeNome() {
        return cidadeNome;
    }

    public void setCidadeNome(String cidadeNome) {
        this.cidadeNome = cidadeNome;
    }

    public Long getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Long estadoId) {
        this.estadoId = estadoId;
    }

    public String getEstadoNome() {
        return estadoNome;
    }

    public void setEstadoNome(String estadoNome) {
        this.estadoNome = estadoNome;
    }

    public Long getBancoId() {
        return bancoId;
    }

    public void setBancoId(Long bancoId) {
        this.bancoId = bancoId;
    }

    public String getBancoNome() {
        return bancoNome;
    }

    public void setBancoNome(String bancoNome) {
        this.bancoNome = bancoNome;
    }    
}