package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.model.Cidade;
import aplicacaofinanceira.repository.CidadeRepository;
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
public class CidadeServiceImpl implements CidadeService {

    @Autowired
    private CidadeRepository cidadeRepository;
    
    @Autowired
    private MessageSource messageSource;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(Long id) throws NotFoundException {
        Cidade cidade = cidadeRepository.findOne(id);

        if (cidade == null) {
            throw new NotFoundException(messageSource.getMessage("cidadeNaoEncontrado", null, null));
        }
        
        cidadeRepository.delete(id);
    }

    @Override
    public Collection<Cidade> findAll() {
        return cidadeRepository.findAll(new Sort(Sort.Direction.ASC, "nome"));
    }    

    @Override
    public Cidade findOne(Long id) throws NotFoundException {
        Cidade cidade = cidadeRepository.findOne(id);

        if (cidade == null) {
            throw new NotFoundException(messageSource.getMessage("cidadeNaoEncontrado", null, null));
        }        
        
        return cidade;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Cidade insert(Cidade cidade) {
        Cidade savedCidade = cidadeRepository.save(cidade);
        
        return savedCidade;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Cidade update(Long id, Cidade cidade) throws NotFoundException {
        Cidade cidadeToUpdate = findOne(id);

        if (cidadeToUpdate == null) {
            throw new NoResultException(messageSource.getMessage("cidadeNaoEncontrado", null, null));
        }
        
        cidadeToUpdate.setNome(cidade.getNome());

        return cidadeRepository.save(cidadeToUpdate);
    }
}
