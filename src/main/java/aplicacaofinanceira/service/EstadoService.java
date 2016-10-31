package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.NotEmptyCollectionException;
import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.NotUniqueException;
import aplicacaofinanceira.model.Estado;
import java.util.Collection;

public interface EstadoService {
    
    void delete(Long id) throws NotEmptyCollectionException, NotFoundException;    
    
    Collection<Estado> findAll();

    Estado findOne(Long id) throws NotFoundException;    
    
    Estado insert(Estado estado) throws NotUniqueException;        
    
    Estado update(Long id, Estado estado) throws NotFoundException, NotUniqueException;
}
