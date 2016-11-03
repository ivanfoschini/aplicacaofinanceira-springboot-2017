package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.NotUniqueException;
import aplicacaofinanceira.model.Agencia;
import aplicacaofinanceira.repository.AgenciaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AgenciaServiceImpl implements AgenciaService {

    @Autowired
    private AgenciaRepository agenciaRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(Long id) throws NotFoundException {
        Agencia agencia = agenciaRepository.findOne(id);

        if (agencia == null) {
            throw new NotFoundException(messageSource.getMessage("agenciaNaoEncontrada", null, null));
        }

        agenciaRepository.delete(agencia);
    }

    @Override
    public List<Agencia> findAll() {
        return agenciaRepository.findAll(new Sort(Sort.Direction.ASC, "nome"));
    }

    @Override
    public Agencia findOne(Long id) throws NotFoundException {
        Agencia agencia = agenciaRepository.findOne(id);

        if (agencia == null) {
            throw new NotFoundException(messageSource.getMessage("agenciaNaoEncontrada", null, null));
        }

        return agencia;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Agencia insert(Agencia agencia) throws NotUniqueException {
        if (!isNumberUnique(agencia.getNumero())) {
            throw new NotUniqueException(messageSource.getMessage("agenciaNumeroDeveSerUnico", null, null));
        }

        Agencia savedAgencia = agenciaRepository.saveAndFlush(agencia);

        return savedAgencia;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Agencia update(Long id, Agencia agencia) throws NotFoundException, NotUniqueException {
        Agencia agenciaToUpdate = findOne(id);

        if (agenciaToUpdate == null) {
            throw new NotFoundException(messageSource.getMessage("agenciaNaoEncontrada", null, null));
        }
        
        if (!isNumberUnique(agencia.getNumero(), agenciaToUpdate.getId())) {
            throw new NotUniqueException(messageSource.getMessage("agenciaNumeroDeveSerUnico", null, null));
        }

        agenciaToUpdate.setNome(agencia.getNome());
        agenciaToUpdate.setNumero(agencia.getNumero());
        agenciaToUpdate.setBanco(agencia.getBanco());
        agenciaToUpdate.getEndereco().setLogradouro(agencia.getEndereco().getLogradouro());
        agenciaToUpdate.getEndereco().setNumero(agencia.getEndereco().getNumero());
        agenciaToUpdate.getEndereco().setComplemento(agencia.getEndereco().getComplemento());
        agenciaToUpdate.getEndereco().setBairro(agencia.getEndereco().getBairro());
        agenciaToUpdate.getEndereco().setCep(agencia.getEndereco().getCep());
        agenciaToUpdate.getEndereco().setCidade(agencia.getEndereco().getCidade());

        return agenciaRepository.save(agenciaToUpdate);
    }

    private boolean isNumberUnique(Integer numero) {
        Agencia agencia = agenciaRepository.findByNumero(numero);
        
        return agencia == null ? true : false;
    }
    
    private boolean isNumberUnique(Integer numero, Long id) {
        Agencia agencia = agenciaRepository.findByNumeroAndDifferentId(numero, id);
        
        return agencia == null ? true : false;
    }
}
