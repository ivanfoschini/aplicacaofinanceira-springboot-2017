package aplicacaofinanceira.repository;

import aplicacaofinanceira.model.ClientePessoaFisica;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientePessoaFisicaRepository extends JpaRepository<ClientePessoaFisica, Long> {
    
}
