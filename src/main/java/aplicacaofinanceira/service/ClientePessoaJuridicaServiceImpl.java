package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.EmptyCollectionException;
import aplicacaofinanceira.exception.NotEmptyCollectionException;
import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.model.ClientePessoaJuridica;
import aplicacaofinanceira.model.Endereco;
import aplicacaofinanceira.repository.ClientePessoaJuridicaRepository;
import aplicacaofinanceira.repository.EnderecoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ClientePessoaJuridicaServiceImpl implements ClientePessoaJuridicaService {
    
    @Autowired
    private ClientePessoaJuridicaRepository clientePessoaJuridicaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;
    
    @Autowired
    private MessageSource messageSource;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(Long id) throws NotEmptyCollectionException, NotFoundException {
        ClientePessoaJuridica clientePessoaJuridica = clientePessoaJuridicaRepository.findOne(id);

        if (clientePessoaJuridica == null) {
            throw new NotFoundException(messageSource.getMessage("clienteNaoEncontrado", null, null));
        }
        
        if (!clientePessoaJuridica.getCorrentistas().isEmpty()) {
            throw new NotEmptyCollectionException(messageSource.getMessage("clienteEhCorrentista", null, null));
        }

        clientePessoaJuridicaRepository.delete(clientePessoaJuridica);
    }

    @Override
    public List<ClientePessoaJuridica> findAll() {
        return clientePessoaJuridicaRepository.findAll(new Sort(Sort.Direction.ASC, "nome"));
    }

    @Override
    public ClientePessoaJuridica findOne(Long id) throws NotFoundException {
        ClientePessoaJuridica clientePessoaJuridica = clientePessoaJuridicaRepository.findOne(id);

        if (clientePessoaJuridica == null) {
            throw new NotFoundException(messageSource.getMessage("clienteNaoEncontrado", null, null));
        }

        return clientePessoaJuridica;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ClientePessoaJuridica insert(ClientePessoaJuridica clientePessoaJuridica) throws EmptyCollectionException {
        if (clientePessoaJuridica.getEnderecos().isEmpty()) {
            throw new EmptyCollectionException(messageSource.getMessage("clienteSemEnderecos", null, null));
        }
        
        ClientePessoaJuridica savedClientePessoaJuridica = clientePessoaJuridicaRepository.save(clientePessoaJuridica);
        
        for (Endereco endereco: clientePessoaJuridica.getEnderecos()) {
            endereco.setCliente(clientePessoaJuridica);
            enderecoRepository.save(endereco);
        }

        return savedClientePessoaJuridica;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ClientePessoaJuridica update(Long id, ClientePessoaJuridica clientePessoaJuridica) throws EmptyCollectionException, NotFoundException {
        if (clientePessoaJuridica.getEnderecos().isEmpty()) {
            throw new EmptyCollectionException(messageSource.getMessage("clienteSemEnderecos", null, null));
        }
        
        ClientePessoaJuridica clientePessoaJuridicaToUpdate = findOne(id);

        if (clientePessoaJuridicaToUpdate == null) {
            throw new NotFoundException(messageSource.getMessage("clienteNaoEncontrado", null, null));
        }
        
        clientePessoaJuridicaToUpdate.setNome(clientePessoaJuridica.getNome());
        clientePessoaJuridicaToUpdate.setStatus(clientePessoaJuridica.getStatus());
        clientePessoaJuridicaToUpdate.setCnpj(clientePessoaJuridica.getCnpj());
        
        for (Endereco endereco: clientePessoaJuridicaToUpdate.getEnderecos()) {
            enderecoRepository.delete(endereco);
        }
        
        ClientePessoaJuridica updatedPessoaJuridica = clientePessoaJuridicaRepository.save(clientePessoaJuridicaToUpdate);
        
        for (Endereco endereco: clientePessoaJuridica.getEnderecos()) {
            endereco.setCliente(clientePessoaJuridicaToUpdate);
            enderecoRepository.save(endereco);
        }

        return updatedPessoaJuridica;
    }
}
