package aplicacaofinanceira.service;

import aplicacaofinanceira.model.Banco;
import java.util.Collection;

public interface BancoService {
    
    void delete(Long id);    
    
    Collection<Banco> findAll();

    Banco findOne(Long id);    
    
    Banco insert(Banco banco);        
    
    Banco update(Long id, Banco banco);
}
