package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.EmptyCollectionException;
import aplicacaofinanceira.exception.NotEmptyCollectionException;
import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.model.ClientePessoaFisica;
import java.util.List;

public interface ClientePessoaFisicaService {

    void delete(Long id) throws NotEmptyCollectionException, NotFoundException;    
    
    List<ClientePessoaFisica> findAll();

    ClientePessoaFisica findOne(Long id) throws NotFoundException;    
    
    ClientePessoaFisica insert(ClientePessoaFisica clientePessoaFisica) throws EmptyCollectionException;        
    
    ClientePessoaFisica update(Long id, ClientePessoaFisica clientePessoaFisica) throws EmptyCollectionException, NotFoundException;    
}