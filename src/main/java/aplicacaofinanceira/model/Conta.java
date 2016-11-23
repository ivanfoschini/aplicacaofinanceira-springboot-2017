package aplicacaofinanceira.model;

import aplicacaofinanceira.util.ContaCorrenteViews;
import aplicacaofinanceira.util.ContaPoupancaViews;
<<<<<<< HEAD
import aplicacaofinanceira.util.CorrentistaViews;
=======
>>>>>>> cliente
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.joda.time.LocalDate;

@Entity
@Table(name = "conta")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo", length = 1)
public abstract class Conta implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @SequenceGenerator(name="Conta_Generator", sequenceName="conta_sequence", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="Conta_Generator")            
    @Column(name = "conta_id", nullable = false)   
    @JsonView({ContaCorrenteViews.ContaCorrenteSimple.class, ContaPoupancaViews.ContaPoupancaSimple.class, CorrentistaViews.CorrentistaSimple.class})
    private Long id;
    
    @NotNull(message = "{contaNumeroNaoPodeSerNulo}")
    @Min(value = 1, message = "{contaNumeroDeveSerMaiorDoQueZero}")
    @Column(name = "numero", nullable = false)
    @JsonView({ContaCorrenteViews.ContaCorrenteSimple.class, ContaPoupancaViews.ContaPoupancaSimple.class, CorrentistaViews.CorrentistaSimple.class})
    private int numero;
    
    @NotNull(message = "{contaSaldoNaoPodeSerNulo}")
    @Column(name = "saldo", nullable = false)
    @JsonView({ContaCorrenteViews.ContaCorrenteSimple.class, ContaPoupancaViews.ContaPoupancaSimple.class})
    private float saldo;
    
    @NotNull(message = "{contaDataDeAberturaNaoPodeSerNula}")
    @Column(name = "data_de_abertura", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)

    @JsonView({ContaCorrenteViews.ContaCorrenteSimple.class, ContaPoupancaViews.ContaPoupancaSimple.class})
    private LocalDate dataDeAbertura;
        
    @JoinColumn(name = "agencia_id", referencedColumnName = "agencia_id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonView({ContaCorrenteViews.ContaCorrenteSimple.class, ContaPoupancaViews.ContaPoupancaSimple.class})
    private Agencia agencia;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "conta", fetch = FetchType.LAZY)
    private Collection<Correntista> correntistas;
        
    public Conta() {}

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

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public LocalDate getDataDeAbertura() {
        return dataDeAbertura;
    }

    public void setDataDeAbertura(LocalDate dataDeAbertura) {
        this.dataDeAbertura = dataDeAbertura;
    }

    public Agencia getAgencia() {
        return agencia;
    }

    public void setAgencia(Agencia agencia) {
        this.agencia = agencia;
    }

    public Collection<Correntista> getCorrentistas() {
        return correntistas;
    }

    public void setCorrentistas(Collection<Correntista> correntistas) {
        this.correntistas = correntistas;
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
        if (!(object instanceof Conta)) {
            return false;
        }
        Conta other = (Conta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }    
}