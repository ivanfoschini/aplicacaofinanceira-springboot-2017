package aplicacaofinanceira.model;

import aplicacaofinanceira.util.ClientePessoaFisicaViews;
import aplicacaofinanceira.validation.Cpf;
import com.fasterxml.jackson.annotation.JsonView;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@DiscriminatorValue(value = "F")
public class ClientePessoaFisica extends Cliente implements Serializable {
    
    @NotNull(message = "{clientePessoaFisicaRgNaoPodeSerNulo}")
    @Column(name = "rg")
    @JsonView(ClientePessoaFisicaViews.ClientePessoaFisicaSimple.class)
    private String rg;
    
    @NotNull(message = "{clientePessoaFisicaCpfNaoPodeSerNulo}")
    @Cpf(message = "{clientePessoaFisicaCpfInvalido}")
    @Column(name = "cpf", length = 11)
    @JsonView(ClientePessoaFisicaViews.ClientePessoaFisicaSimple.class)
    private String cpf;

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}