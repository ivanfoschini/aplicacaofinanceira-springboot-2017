package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.NotUniqueException;
import aplicacaofinanceira.model.Estado;
import aplicacaofinanceira.repository.EstadoRepository;
import java.util.Collection;
import javax.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class EstadoServiceImpl implements EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;
    
    @Autowired
    private MessageSource messageSource;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(Long id) throws NotFoundException {
        Estado estado = estadoRepository.findOne(id);

        if (estado == null) {
            throw new NotFoundException(messageSource.getMessage("estadoNaoEncontrado", null, null));
        }
        
        estadoRepository.delete(id);
    }

    @Override
    public Collection<Estado> findAll() {
        return estadoRepository.findAll(new Sort(Sort.Direction.ASC, "nome"));
    }    

    @Override
    public Estado findOne(Long id) throws NotFoundException {
        Estado estado = estadoRepository.findOne(id);

        if (estado == null) {
            throw new NotFoundException(messageSource.getMessage("estadoNaoEncontrado", null, null));
        }        
        
        return estado;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Estado insert(Estado estado) throws NotUniqueException {
        if (!EstadoServiceImpl.this.isNomeUnique(estado.getNome())) {
            throw new NotUniqueException(messageSource.getMessage("estadoNomeDeveSerUnico", null, null));
        }

        return estadoRepository.save(estado);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Estado update(Long id, Estado estado) throws NotFoundException, NotUniqueException {
        Estado estadoToUpdate = findOne(id);

        if (estadoToUpdate == null) {
            throw new NoResultException(messageSource.getMessage("estadoNaoEncontrado", null, null));
        }
        
        if (!isNomeUnique(estado.getNome(), estadoToUpdate.getId())) {
            throw new NotUniqueException(messageSource.getMessage("estadoNomeDeveSerUnico", null, null));
        }

        estadoToUpdate.setNome(estado.getNome());

        return estadoRepository.save(estadoToUpdate);
    }

    private boolean isNomeUnique(String nome) {
        Estado estado = estadoRepository.findByNome(nome);
        
        return estado == null ? true : false;
    }
    
    private boolean isNomeUnique(String nome, Long id) {
        Estado estado = estadoRepository.findByNomeAndDifferentId(nome, id);
        
        return estado == null ? true : false;
    }
}
