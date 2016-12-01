package aplicacaofinanceira.model;

import aplicacaofinanceira.validation.Cep;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "endereco")
public class Endereco implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @SequenceGenerator(name="Endereco_Generator", sequenceName="endereco_sequence", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="Endereco_Generator")    
    @Column(name = "endereco_id", nullable = false)    
    private Long id;
    
    @NotNull(message = "{enderecoLogradouroNaoPodeSerNulo}")
    @Size(min = 2, max = 255, message = "{enderecoLogradouroDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres}")
    @Column(name = "logradouro", nullable = false, length = 255)
    private String logradouro;
    
    @NotNull(message = "{enderecoNumeroNaoPodeSerNulo}")
    @Min(value = 1, message = "{enderecoNumeroDeveSerMaiorDoQueZero}")
    @Column(name = "numero", nullable = false)
    private int numero;
    
    @Size(max = 255, message = "{enderecoComplementoDeveTerNoMaximoDuzentosECinquentaECincoCaracteres}")
    @Column(name = "complemento", length = 255)
    private String complemento;
    
    @NotNull(message = "{enderecoBairroNaoPodeSerNulo}")
    @Size(min = 2, max = 255, message = "{enderecoBairroDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres}")    
    @Column(name = "bairro", nullable = false, length = 255)
    private String bairro;
    
    @NotNull(message = "{enderecoCepNaoPodeSerNulo}")
    @Cep(message = "{enderecoCepInvalido}")
    @Column(name = "cep", nullable = false, length = 9)
    private String cep;
    
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "endereco", fetch = FetchType.LAZY)
    private Agencia agencia;
    
    @JoinColumn(name = "cidade_id", referencedColumnName = "cidade_id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Cidade cidade;

    public Endereco() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Agencia getAgencia() {
        return agencia;
    }

    public void setAgencia(Agencia agencia) {
        this.agencia = agencia;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Endereco)) {
            return false;
        }
        Endereco other = (Endereco) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }    
}