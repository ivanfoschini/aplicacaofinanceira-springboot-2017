package aplicacaofinanceira.model;

import aplicacaofinanceira.util.ContaPoupancaViews;
import com.fasterxml.jackson.annotation.JsonView;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.joda.time.LocalDate;

@Entity
@Table(name = "conta_poupanca")
@DiscriminatorValue(value = "P")
public class ContaPoupanca extends Conta implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "{contaPoupancaDataDeAniversarioNaoPodeSerNula}")
    @Column(name = "data_de_aniversario", nullable = false)
    @JsonView(ContaPoupancaViews.ContaPoupancaSimple.class)
    private LocalDate dataDeAniversario;
    
    @NotNull(message = "{contaPoupancaCorrecaoMonetariaNaoPodeSerNula}")
    @Min(value = 0, message = "{contaPoupancaCorrecaoMonetariaDeveSerMaiorOuIgualAZero}")
    @Column(name = "correcao_monetaria", nullable = false)
    @JsonView(ContaPoupancaViews.ContaPoupancaSimple.class)
    private float correcaoMonetaria;
    
    @NotNull(message = "{contaPoupancaJurosNaoPodeSerNulo}")
    @Min(value = 0, message = "{contaPoupancaJurosDeveSerMaiorOuIgualAZero}")
    @Column(name = "juros", nullable = false)
    @JsonView(ContaPoupancaViews.ContaPoupancaSimple.class)
    private float juros;

    public ContaPoupanca() {}

    public LocalDate getDataDeAniversario() {
        return dataDeAniversario;
    }

    public void setDataDeAniversario(LocalDate dataDeAniversario) {
        this.dataDeAniversario = dataDeAniversario;
    }

    public float getCorrecaoMonetaria() {
        return correcaoMonetaria;
    }

    public void setCorrecaoMonetaria(float correcaoMonetaria) {
        this.correcaoMonetaria = correcaoMonetaria;
    }

    public float getJuros() {
        return juros;
    }

    public void setJuros(float juros) {
        this.juros = juros;
    }
}
