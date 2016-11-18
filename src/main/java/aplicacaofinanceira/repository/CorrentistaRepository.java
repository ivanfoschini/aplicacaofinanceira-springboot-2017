package aplicacaofinanceira.repository;

import aplicacaofinanceira.model.Correntista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorrentistaRepository extends JpaRepository<Correntista, Long> {
    
}