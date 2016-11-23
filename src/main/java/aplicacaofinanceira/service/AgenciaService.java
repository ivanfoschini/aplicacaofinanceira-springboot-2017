package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.NotEmptyCollectionException;
import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.NotUniqueException;
import aplicacaofinanceira.model.Agencia;
import java.util.List;

public interface AgenciaService {
    
    void delete(Long id) throws NotEmptyCollectionException, NotFoundException;    
    
    List<Agencia> findAll();

    Agencia findOne(Long id) throws NotFoundException;    
    
    Agencia insert(Agencia agencia) throws NotUniqueException;        
    
    Agencia update(Long id, Agencia agencia) throws NotFoundException, NotUniqueException;
}
