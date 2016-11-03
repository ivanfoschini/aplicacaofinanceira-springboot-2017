package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.NotUniqueException;
import aplicacaofinanceira.model.Cidade;
import aplicacaofinanceira.repository.CidadeRepository;
import java.util.List;
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
            throw new NotFoundException(messageSource.getMessage("cidadeNaoEncontrada", null, null));
        }

        cidadeRepository.delete(cidade);
    }

    @Override
    public List<Cidade> findAll() {
        return cidadeRepository.findAll(new Sort(Sort.Direction.ASC, "nome"));
    }

    @Override
    public Cidade findOne(Long id) throws NotFoundException {
        Cidade cidade = cidadeRepository.findOne(id);

        if (cidade == null) {
            throw new NotFoundException(messageSource.getMessage("cidadeNaoEncontrada", null, null));
        }

        return cidade;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Cidade insert(Cidade cidade) throws NotUniqueException {
        if (!isNomeUniqueForEstado(cidade.getNome(), cidade.getEstado().getId())) {
            throw new NotUniqueException(messageSource.getMessage("cidadeNomeDeveSerUnicoParaEstado", null, null));
        }

        Cidade savedCidade = cidadeRepository.save(cidade);

        return savedCidade;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Cidade update(Long id, Cidade cidade) throws NotFoundException, NotUniqueException {
        Cidade cidadeToUpdate = findOne(id);

        if (cidadeToUpdate == null) {
            throw new NotFoundException(messageSource.getMessage("cidadeNaoEncontrada", null, null));
        }
        
        if (!isNomeUniqueForEstado(cidade.getNome(), cidade.getEstado().getId(), cidadeToUpdate.getId())) {
            throw new NotUniqueException(messageSource.getMessage("cidadeNomeDeveSerUnicoParaEstado", null, null));
        }

        cidadeToUpdate.setNome(cidade.getNome());
        cidadeToUpdate.setEstado(cidade.getEstado());

        return cidadeRepository.save(cidadeToUpdate);
    }

    private boolean isNomeUniqueForEstado(String nomeDaCidade, Long idDoEstado) {        
        Cidade cidade = cidadeRepository.findByNomeAndEstado(nomeDaCidade, idDoEstado);

        return cidade != null ? false : true;        
    }

    private boolean isNomeUniqueForEstado(String nomeDaCidade, Long idDoEstadoToUpdate, Long idDaCidadeCurrent) {
        Cidade cidade = cidadeRepository.findByNomeAndEstadoAndDifferentId(nomeDaCidade, idDoEstadoToUpdate, idDaCidadeCurrent);

        return cidade != null ? false : true;
    }
}