package aplicacaofinanceira.repository;

import aplicacaofinanceira.model.Agencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AgenciaRepository extends JpaRepository<Agencia, Long> {        
    
    @Query("select agencia from Agencia agencia where agencia.numero = :numero")
    Agencia findByNumero(@Param("numero") Integer numero);
    
    @Query("select agencia from Agencia agencia where agencia.numero = :numero and agencia.id <> :id")
    Agencia findByNumeroAndDifferentId(@Param("numero") Integer numero, @Param("id") Long id);
}
