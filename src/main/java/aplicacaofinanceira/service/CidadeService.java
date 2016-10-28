package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.NotUniqueException;
import aplicacaofinanceira.model.Cidade;
import java.util.Collection;

public interface CidadeService {
    
    void delete(Long id) throws NotFoundException;    
    
    Collection<Cidade> findAll();

    Cidade findOne(Long id) throws NotFoundException;    
    
    Cidade insert(Cidade cidade) throws NotUniqueException;        
    
    Cidade update(Long id, Cidade cidade) throws NotFoundException;
}
