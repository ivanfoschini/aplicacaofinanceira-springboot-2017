package aplicacaofinanceira.repository;

import aplicacaofinanceira.model.ContaCorrente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContaCorrenteRepository extends JpaRepository<ContaCorrente, Long> {
    
//    @Query("select contaCorrente from ContaCorrente contaCorrente where contaCorrente.numero = :numero")
//    ContaCorrente findByNumero(@Param("numero") Integer numero);
//    
//    @Query("select contaCorrente from ContaCorrente contaCorrente where contaCorrente.numero = :numero and contaCorrente.id <> :id")
//    ContaCorrente findByNumeroAndDifferentId(@Param("numero") Integer numero, @Param("id") Long id);    
}