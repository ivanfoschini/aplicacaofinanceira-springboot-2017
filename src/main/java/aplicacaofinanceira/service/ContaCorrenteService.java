package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.NotEmptyCollectionException;
import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.NotUniqueException;
import aplicacaofinanceira.model.ContaCorrente;
import java.util.List;

public interface ContaCorrenteService {
    
    void delete(Long id) throws NotEmptyCollectionException, NotFoundException;    
    
    List<ContaCorrente> findAll();

    ContaCorrente findOne(Long id) throws NotFoundException;    
    
    ContaCorrente insert(ContaCorrente contaCorrente) throws NotUniqueException;        
    
    ContaCorrente update(Long id, ContaCorrente contaCorrente) throws NotFoundException, NotUniqueException;
}
