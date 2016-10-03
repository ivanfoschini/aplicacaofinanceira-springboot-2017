package aplicacaofinanceira.service;

import aplicacaofinanceira.model.Banco;
import java.util.Collection;

public interface BancoService {
    
    void delete(Long id);    
    
    Collection<Banco> findAll();

    Banco findById(Long id);    
    
    Banco insert(Banco banco);        
    
    Banco update(Long id, Banco banco);
}
