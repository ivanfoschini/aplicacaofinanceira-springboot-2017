package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.NotUniqueException;
import aplicacaofinanceira.model.Estado;
import java.util.List;

public interface EstadoService {
    
    void delete(Long id) throws NotFoundException;    
    
    List<Estado> findAll();

    Estado findOne(Long id) throws NotFoundException;    
    
    Estado insert(Estado estado) throws NotUniqueException;        
    
    Estado update(Long id, Estado estado) throws NotFoundException, NotUniqueException;
}
