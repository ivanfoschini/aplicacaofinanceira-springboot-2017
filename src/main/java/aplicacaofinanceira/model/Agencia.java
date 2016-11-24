package aplicacaofinanceira.model;

import aplicacaofinanceira.util.AgenciaViews;
import com.fasterxml.jackson.annotation.JsonView;
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
@Table(name = "agencia")
public class Agencia implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @SequenceGenerator(name="Agencia_Generator", sequenceName="agencia_sequence", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="Agencia_Generator")
    @Column(name = "agencia_id", nullable = false)  
    @JsonView(AgenciaViews.AgenciaSimple.class)
    private Long id;
    
    @NotNull(message = "{agenciaNumeroNaoPodeSerNulo}")
    @Min(value = 1, message = "{agenciaNumeroDeveSerMaiorDoQueZero}")
    @Column(name = "numero", nullable = false, unique = true)
    @JsonView(AgenciaViews.AgenciaSimple.class)
    private int numero;
    
    @NotNull(message = "{agenciaNomeNaoPodeSerNulo}")
    @Size(min = 2, max = 255, message = "{agenciaNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres}")
    @Column(name = "nome", nullable = false, length = 255)
    @JsonView(AgenciaViews.AgenciaSimple.class)
    private String nome;
    
    @JoinColumn(name = "endereco_id", referencedColumnName = "endereco_id", nullable = false)
    @OneToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
    private Endereco endereco;
    
    @JoinColumn(name = "banco_id", referencedColumnName = "banco_id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Banco banco;
    
    public Agencia() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
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
        if (!(object instanceof Agencia)) {
            return false;
        }
        Agencia other = (Agencia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
