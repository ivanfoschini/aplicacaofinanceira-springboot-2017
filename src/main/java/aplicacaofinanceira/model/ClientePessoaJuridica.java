package aplicacaofinanceira.model;

import aplicacaofinanceira.util.ClientePessoaJuridicaViews;
import aplicacaofinanceira.validation.Cnpj;
import com.fasterxml.jackson.annotation.JsonView;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@DiscriminatorValue(value = "J")
public class ClientePessoaJuridica extends Cliente implements Serializable {
    
    @NotNull(message = "{clientePessoaJuridicaCnpjNaoPodeSerNulo}")
    @Cnpj(message = "{clientePessoaJuridicaCnpjInvalido}")
    @Column(name = "cnpj", length = 14)
    @JsonView(ClientePessoaJuridicaViews.ClientePessoaJuridicaSimple.class)
    private String cnpj;

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }    
}