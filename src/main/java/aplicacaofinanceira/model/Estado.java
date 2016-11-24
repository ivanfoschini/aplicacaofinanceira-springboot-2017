package aplicacaofinanceira.model;

import aplicacaofinanceira.util.EstadoViews;
import com.fasterxml.jackson.annotation.JsonView;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "estado")
public class Estado implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @SequenceGenerator(name = "Estado_Generator", sequenceName = "estado_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Estado_Generator")
    @Column(name = "estado_id", nullable = false)
    @JsonView(EstadoViews.EstadoSimple.class)
    private Long id;
    
    @NotNull(message = "{estadoNomeNaoPodeSerNulo}")
    @Size(min = 2, max = 255, message = "{estadoNomeDeveTerEntreDoisEDuzentosECinquentaECincoCaracteres}")
    @Column(name = "nome", nullable = false, length = 255, unique = true)
    @JsonView(EstadoViews.EstadoSimple.class)
    private String nome;

    public Estado() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof Estado)) {
            return false;
        }
        Estado other = (Estado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}