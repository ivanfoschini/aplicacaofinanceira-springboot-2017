package aplicacaofinanceira.repository;

import aplicacaofinanceira.model.ClientePessoaJuridica;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientePessoaJuridicaRepository extends JpaRepository<ClientePessoaJuridica, Long> {
    
}