package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.NotUniqueException;
import aplicacaofinanceira.model.ContaPoupanca;
import java.util.List;

public interface ContaPoupancaService {
    
    void delete(Long id) throws NotFoundException;    
    
    List<ContaPoupanca> findAll();

    ContaPoupanca findOne(Long id) throws NotFoundException;    
    
    ContaPoupanca insert(ContaPoupanca contaPoupanca) throws NotFoundException, NotUniqueException;        
    
    ContaPoupanca update(Long id, ContaPoupanca contaPoupanca) throws NotFoundException, NotUniqueException;
}
