package aplicacaofinanceira.model;

import aplicacaofinanceira.util.ContaCorrenteViews;
import com.fasterxml.jackson.annotation.JsonView;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "conta_corrente")
@DiscriminatorValue(value = "C")
public class ContaCorrente extends Conta implements Serializable {
 
    private static final long serialVersionUID = 1L;
    
    @Column(name = "limite", nullable = false)
    @JsonView(ContaCorrenteViews.ContaCorrenteSimple.class)
    private float limite;

    public ContaCorrente() {}

    public float getLimite() {
        return limite;
    }

    public void setLimite(float limite) {
        this.limite = limite;
    }    
}
