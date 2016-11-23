package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.EmptyCollectionException;
import aplicacaofinanceira.exception.NotEmptyCollectionException;
import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.model.ClientePessoaJuridica;
import java.util.List;

public interface ClientePessoaJuridicaService {
 
    void delete(Long id) throws NotEmptyCollectionException, NotFoundException;    
    
    List<ClientePessoaJuridica> findAll();

    ClientePessoaJuridica findOne(Long id) throws NotFoundException;    
    
    ClientePessoaJuridica insert(ClientePessoaJuridica clientePessoaJuridica) throws EmptyCollectionException;        
    
    ClientePessoaJuridica update(Long id, ClientePessoaJuridica clientePessoaJuridica) throws EmptyCollectionException, NotFoundException; 
}