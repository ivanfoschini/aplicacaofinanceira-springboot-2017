package aplicacaofinanceira.repository;

import aplicacaofinanceira.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {
    
    @Query("select estado from Estado estado where estado.nome =:nome")
    Estado findByNome(@Param("nome") String nome);
    
    @Query("select estado from Estado estado where estado.nome =:nome and estado.id <>:id")
    Estado findByNomeAndDifferentId(@Param("nome") String nome, @Param("id") Long id);
}