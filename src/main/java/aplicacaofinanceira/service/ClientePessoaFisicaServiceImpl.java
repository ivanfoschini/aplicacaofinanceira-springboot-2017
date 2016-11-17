package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.EmptyCollectionException;
import aplicacaofinanceira.model.ClientePessoaFisica;
import aplicacaofinanceira.model.Endereco;
import aplicacaofinanceira.repository.ClientePessoaFisicaRepository;
import aplicacaofinanceira.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ClientePessoaFisicaServiceImpl implements ClientePessoaFisicaService {

    @Autowired
    private ClientePessoaFisicaRepository clientePessoaFisicaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;
    
    @Autowired
    private MessageSource messageSource;

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
//    public void delete(Long id) throws NotFoundException {
//        ClientePessoaFisica clientePessoaFisica = clientePessoaFisicaRepository.findOne(id);
//
//        if (clientePessoaFisica == null) {
//            throw new NotFoundException(messageSource.getMessage("clienteNaoEncontrado", null, null));
//        }
//
//        clientePessoaFisicaRepository.delete(clientePessoaFisica);
//    }
//
//    @Override
//    public List<ClientePessoaFisica> findAll() {
//        return clientePessoaFisicaRepository.findAll(new Sort(Sort.Direction.ASC, "numero"));
//    }
//
//    @Override
//    public ClientePessoaFisica findOne(Long id) throws NotFoundException {
//        ClientePessoaFisica clientePessoaFisica = clientePessoaFisicaRepository.findOne(id);
//
//        if (clientePessoaFisica == null) {
//            throw new NotFoundException(messageSource.getMessage("clienteNaoEncontrado", null, null));
//        }
//
//        return clientePessoaFisica;
//    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ClientePessoaFisica insert(ClientePessoaFisica clientePessoaFisica) throws EmptyCollectionException {
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

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
//    public ClientePessoaFisica update(Long id, ClientePessoaFisica clientePessoaFisica) throws NotFoundException, NotUniqueException {
//        ClientePessoaFisica clientePessoaFisicaToUpdate = findOne(id);
//
//        if (clientePessoaFisicaToUpdate == null) {
//            throw new NotFoundException(messageSource.getMessage("clienteNaoEncontrado", null, null));
//        }
//        
//        if (!isNumberUnique(clientePessoaFisica.getNumero(), clientePessoaFisicaToUpdate.getId())) {
//            throw new NotUniqueException(messageSource.getMessage("clienteNumeroDeveSerUnico", null, null));
//        }
//
//        clientePessoaFisicaToUpdate.setNumero(clientePessoaFisica.getNumero());
//        clientePessoaFisicaToUpdate.setSaldo(clientePessoaFisica.getSaldo());
//        clientePessoaFisicaToUpdate.setDataDeAbertura(clientePessoaFisica.getDataDeAbertura());
//        clientePessoaFisicaToUpdate.setLimite(clientePessoaFisica.getLimite());
//        clientePessoaFisicaToUpdate.setAgencia(clientePessoaFisica.getAgencia());
//
//        return clientePessoaFisicaRepository.save(clientePessoaFisicaToUpdate);
//    }    
}