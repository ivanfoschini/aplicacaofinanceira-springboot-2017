package aplicacaofinanceira.model;

import aplicacaofinanceira.util.BancoViews;
import aplicacaofinanceira.validation.Cnpj;
import com.fasterxml.jackson.annotation.JsonView;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "banco")
public class Banco implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @SequenceGenerator(name = "Banco_Generator", sequenceName = "banco_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Banco_Generator")
    @Column(name = "banco_id", nullable = false)
    @JsonView(BancoViews.BancoSimple.class)
    private Long id;
    
    @NotNull(message = "{bancoNumeroNaoPodeSerNulo}")
    @Min(value = 1, message = "{bancoNumeroDeveSerMaiorDoQueZero}")
    @Column(name = "numero", nullable = false, unique = true)
    @JsonView(BancoViews.BancoSimple.class)
    private int numero;
   
    @NotNull(message = "{bancoCnpjNaoPodeSerNulo}")
    @Cnpj(message = "{bancoCnpjInvalido}")
    @Column(name = "cnpj", nullable = false, length = 14)    
    @JsonView(BancoViews.BancoSimple.class)
    private String cnpj;
    
    @NotNull(message = "{bancoNomeNaoPodeSerNulo}")
    @Size(min = 2, max = 255, message = "{bancoNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres}")
    @Column(name = "nome", nullable = false, length = 255)
    @JsonView(BancoViews.BancoSimple.class)
    private String nome;

    public Banco() {}

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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
        if (!(object instanceof Banco)) {
            return false;
        }
        Banco other = (Banco) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
