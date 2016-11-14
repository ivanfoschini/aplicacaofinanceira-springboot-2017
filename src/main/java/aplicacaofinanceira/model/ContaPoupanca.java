package aplicacaofinanceira.model;

import aplicacaofinanceira.util.ContaPoupancaViews;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "conta_poupanca")
@DiscriminatorValue(value = "P")
public class ContaPoupanca extends Conta implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "{contaPoupancaDataDeAniversarioNaoPodeSerNula}")
    @Column(name = "data_de_aniversario", nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "America/Sao_Paulo")
    @JsonView(ContaPoupancaViews.ContaPoupancaSimple.class)
    private Date dataDeAniversario;
    
    @NotNull(message = "{contaPoupancaCorrecaoMonetariaNaoPodeSerNula}")
    @Min(value = 0, message = "{contaPoupancaCorrecaoMonetariaDeveSerMaiorOuIgualAZero}")
    @Column(name = "correcao_monetaria", nullable = false)
    @JsonView(ContaPoupancaViews.ContaPoupancaSimple.class)
    private int correcaoMonetaria;
    
    @NotNull(message = "{contaPoupancaJurosNaoPodeSerNulo}")
    @Min(value = 0, message = "{contaPoupancaJurosDeveSerMaiorOuIgualAZero}")
    @Column(name = "juros", nullable = false)
    @JsonView(ContaPoupancaViews.ContaPoupancaSimple.class)
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
