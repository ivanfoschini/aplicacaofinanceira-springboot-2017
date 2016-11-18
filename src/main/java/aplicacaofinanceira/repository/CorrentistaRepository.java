package aplicacaofinanceira.repository;

import aplicacaofinanceira.model.Conta;
import aplicacaofinanceira.model.Correntista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CorrentistaRepository extends JpaRepository<Correntista, Long> {

    @Transactional
    Long deleteByConta(Conta conta);
}