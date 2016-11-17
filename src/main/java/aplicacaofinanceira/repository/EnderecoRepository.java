package aplicacaofinanceira.repository;

import aplicacaofinanceira.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    
}
