package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.EmptyCollectionException;
import aplicacaofinanceira.exception.NotEmptyCollectionException;
import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.model.Cidade;
import aplicacaofinanceira.model.ClientePessoaFisica;
import aplicacaofinanceira.model.Endereco;
import aplicacaofinanceira.repository.CidadeRepository;
import aplicacaofinanceira.repository.ClientePessoaFisicaRepository;
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
public class ClientePessoaFisicaServiceImpl implements ClientePessoaFisicaService {

    @Autowired
    private CidadeRepository cidadeRepository;
    
    @Autowired
    private ClientePessoaFisicaRepository clientePessoaFisicaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;
    
    @Autowired
    private MessageSource messageSource;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(Long id) throws NotEmptyCollectionException, NotFoundException {
        ClientePessoaFisica clientePessoaFisica = clientePessoaFisicaRepository.findOne(id);

        if (clientePessoaFisica == null) {
            throw new NotFoundException(messageSource.getMessage("clienteNaoEncontrado", null, null));
        }
        
        if (!clientePessoaFisica.getCorrentistas().isEmpty()) {
            throw new NotEmptyCollectionException(messageSource.getMessage("clienteEhCorrentista", null, null));
        }    
            
        for (Endereco endereco: clientePessoaFisica.getEnderecos()) {
            enderecoRepository.delete(endereco);
        }

        clientePessoaFisicaRepository.delete(clientePessoaFisica);
    }

    @Override
    public List<ClientePessoaFisica> findAll() {
        return clientePessoaFisicaRepository.findAll(new Sort(Sort.Direction.ASC, "nome"));
    }

    @Override
    public ClientePessoaFisica findOne(Long id) throws NotFoundException {
        ClientePessoaFisica clientePessoaFisica = clientePessoaFisicaRepository.findOne(id);

        if (clientePessoaFisica == null) {
            throw new NotFoundException(messageSource.getMessage("clienteNaoEncontrado", null, null));
        }

        return clientePessoaFisica;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ClientePessoaFisica insert(ClientePessoaFisica clientePessoaFisica) throws EmptyCollectionException, NotFoundException {
        validateCidades(clientePessoaFisica);
        
        if (clientePessoaFisica.getEnderecos().isEmpty()) {
            throw new EmptyCollectionException(messageSource.getMessage("clienteSemEnderecos", null, null));
        }
        
        ClientePessoaFisica savedClientePessoaFisica = clientePessoaFisicaRepository.save(clientePessoaFisica);
        
        for (Endereco endereco: clientePessoaFisica.getEnderecos()) {
            endereco.setCliente(clientePessoaFisica);
            enderecoRepository.save(endereco);
        }

        return savedClientePessoaFisica;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ClientePessoaFisica update(Long id, ClientePessoaFisica clientePessoaFisica) throws EmptyCollectionException, NotFoundException {
        validateCidades(clientePessoaFisica);
        
        if (clientePessoaFisica.getEnderecos().isEmpty()) {
            throw new EmptyCollectionException(messageSource.getMessage("clienteSemEnderecos", null, null));
        }
        
        ClientePessoaFisica clientePessoaFisicaToUpdate = findOne(id);

        if (clientePessoaFisicaToUpdate == null) {
            throw new NotFoundException(messageSource.getMessage("clienteNaoEncontrado", null, null));
        }
        
        clientePessoaFisicaToUpdate.setNome(clientePessoaFisica.getNome());
        clientePessoaFisicaToUpdate.setStatus(clientePessoaFisica.getStatus());
        clientePessoaFisicaToUpdate.setRg(clientePessoaFisica.getRg());
        clientePessoaFisicaToUpdate.setCpf(clientePessoaFisica.getCpf());
        
        for (Endereco endereco: clientePessoaFisicaToUpdate.getEnderecos()) {
            enderecoRepository.delete(endereco);
        }
        
        ClientePessoaFisica updatedPessoaFisica = clientePessoaFisicaRepository.save(clientePessoaFisicaToUpdate);
        
        for (Endereco endereco: clientePessoaFisica.getEnderecos()) {
            endereco.setCliente(clientePessoaFisicaToUpdate);
            enderecoRepository.save(endereco);
        }

        return updatedPessoaFisica;
    }    

    private void validateCidades(ClientePessoaFisica clientePessoaFisica) throws NotFoundException {
        for (Endereco endereco: clientePessoaFisica.getEnderecos()) {
            Cidade cidade = cidadeRepository.findOne(endereco.getCidade().getId());
            
            if (cidade == null) {
                throw new NotFoundException(messageSource.getMessage("cidadeNaoEncontrada", null, null));
            }        
        }
    }
}