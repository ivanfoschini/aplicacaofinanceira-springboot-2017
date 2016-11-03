package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.NotEmptyCollectionException;
import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.NotUniqueException;
import aplicacaofinanceira.model.Banco;
import aplicacaofinanceira.repository.BancoRepository;
import java.util.List;
import javax.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class BancoServiceImpl implements BancoService {

    @Autowired
    private BancoRepository bancoRepository;
    
    @Autowired
    private MessageSource messageSource;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(Long id) throws NotEmptyCollectionException, NotFoundException {
        Banco banco = bancoRepository.findOne(id);

        if (banco == null) {
            throw new NotFoundException(messageSource.getMessage("bancoNaoEncontrado", null, null));
        }
        
        if (!banco.getAgencias().isEmpty()) {
            throw new NotEmptyCollectionException(messageSource.getMessage("bancoPossuiAgencias", null, null));
        }
        
        bancoRepository.delete(id);
    }

    @Override
    public List<Banco> findAll() {
        return bancoRepository.findAll(new Sort(Sort.Direction.ASC, "nome"));
    }    

    @Override
    public Banco findOne(Long id) throws NotFoundException {
        Banco banco = bancoRepository.findOne(id);

        if (banco == null) {
            throw new NotFoundException(messageSource.getMessage("bancoNaoEncontrado", null, null));
        }        
        
        return banco;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Banco insert(Banco banco) throws NotUniqueException {
        if (!isNumberUnique(banco.getNumero())) {
            throw new NotUniqueException(messageSource.getMessage("bancoNumeroDeveSerUnico", null, null));
        }

        return bancoRepository.save(banco);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Banco update(Long id, Banco banco) throws NotFoundException, NotUniqueException {
        Banco bancoToUpdate = findOne(id);

        if (bancoToUpdate == null) {
            throw new NoResultException(messageSource.getMessage("bancoNaoEncontrado", null, null));
        }
        
        if (!isNumberUnique(banco.getNumero(), bancoToUpdate.getId())) {
            throw new NotUniqueException(messageSource.getMessage("bancoNumeroDeveSerUnico", null, null));
        }

        bancoToUpdate.setNumero(banco.getNumero());
        bancoToUpdate.setCnpj(banco.getCnpj());
        bancoToUpdate.setNome(banco.getNome());

        return bancoRepository.save(bancoToUpdate);
    }

    private boolean isNumberUnique(Integer numero) {
        Banco banco = bancoRepository.findByNumero(numero);
        
        return banco == null ? true : false;
    }
    
    private boolean isNumberUnique(Integer numero, Long id) {
        Banco banco = bancoRepository.findByNumeroAndDifferentId(numero, id);
        
        return banco == null ? true : false;
    }
}
