package aplicacaofinanceira.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "conta_poupanca")
@DiscriminatorValue(value = "P")
public class ContaPoupanca extends Conta implements Serializable {
    
    private static final long serialVersionUID = 1L;
        
    @Column(name = "data_de_aniversario", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataDeAniversario;
    
    @Column(name = "correcao_monetaria", nullable = false)
    private int correcaoMonetaria;
    
    @Column(name = "juros", nullable = false)
    private int juros;

    public ContaPoupanca() {}


    public Date getDataDeAniversario() {
        return dataDeAniversario;
    }

    public void setDataDeAniversario(Date dataDeAniversario) {
        this.dataDeAniversario = dataDeAniversario;
    }

    public int getCorrecaoMonetaria() {
        return correcaoMonetaria;
    }

    public void setCorrecaoMonetaria(int correcaoMonetaria) {
        this.correcaoMonetaria = correcaoMonetaria;
    }

    public int getJuros() {
        return juros;
    }

    public void setJuros(int juros) {
        this.juros = juros;
    }
}
