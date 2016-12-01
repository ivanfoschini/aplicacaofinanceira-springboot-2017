package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.NotEmptyCollectionException;
import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.NotUniqueException;
import aplicacaofinanceira.model.ContaPoupanca;
import java.util.List;

public interface ContaPoupancaService {
    
    void delete(Long id) throws NotEmptyCollectionException, NotFoundException;    
    
    List<ContaPoupanca> findAll();

    ContaPoupanca findOne(Long id) throws NotFoundException;    
    
    ContaPoupanca insert(ContaPoupanca contaPoupanca) throws NotFoundException, NotUniqueException;        
    
    ContaPoupanca update(Long id, ContaPoupanca contaPoupanca) throws NotFoundException, NotUniqueException;
}
