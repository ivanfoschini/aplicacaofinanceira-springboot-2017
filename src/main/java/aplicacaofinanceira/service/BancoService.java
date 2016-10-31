package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.NotUniqueException;
import aplicacaofinanceira.model.Banco;
import java.util.List;

public interface BancoService {
    
    void delete(Long id) throws NotFoundException;    
    
    List<Banco> findAll();

    Banco findOne(Long id) throws NotFoundException;    
    
    Banco insert(Banco banco) throws NotUniqueException;        
    
    Banco update(Long id, Banco banco) throws NotFoundException, NotUniqueException;
}
