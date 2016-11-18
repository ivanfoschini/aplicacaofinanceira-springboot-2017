package aplicacaofinanceira.repository;

import aplicacaofinanceira.model.Conta;
import aplicacaofinanceira.model.Correntista;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CorrentistaRepository extends JpaRepository<Correntista, Long> {

    @Transactional
    Long deleteByConta(Conta conta);

    @Query("select correntista from Correntista correntista left join fetch correntista.cliente cliente where cliente.id = :idDoCliente")
    List<Correntista> findByCliente(@Param("idDoCliente") Long idDoCliente);

    @Query("select correntista from Correntista correntista left join fetch correntista.conta conta where conta.id = :idDaConta")
    List<Correntista> findByConta(@Param("idDaConta") Long idDaConta);
}