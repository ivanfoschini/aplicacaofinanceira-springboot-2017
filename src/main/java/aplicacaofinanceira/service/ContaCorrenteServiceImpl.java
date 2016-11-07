package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.NotUniqueException;
import aplicacaofinanceira.model.ContaCorrente;
import aplicacaofinanceira.repository.ContaCorrenteRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ContaCorrenteServiceImpl implements ContaCorrenteService {

    @Autowired
    private ContaCorrenteRepository contaCorrenteRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(Long id) throws NotFoundException {
        ContaCorrente contaCorrente = contaCorrenteRepository.findOne(id);

        if (contaCorrente == null) {
            throw new NotFoundException(messageSource.getMessage("contaCorrenteNaoEncontrada", null, null));
        }

        contaCorrenteRepository.delete(contaCorrente);
    }

    @Override
    public List<ContaCorrente> findAll() {
        return contaCorrenteRepository.findAll(new Sort(Sort.Direction.ASC, "numero"));
    }

    @Override
    public ContaCorrente findOne(Long id) throws NotFoundException {
        ContaCorrente contaCorrente = contaCorrenteRepository.findOne(id);

        if (contaCorrente == null) {
            throw new NotFoundException(messageSource.getMessage("contaCorrenteNaoEncontrada", null, null));
        }

        return contaCorrente;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ContaCorrente insert(ContaCorrente contaCorrente) throws NotUniqueException {
//        if (!isNumberUnique(contaCorrente.getNumero())) {
//            throw new NotUniqueException(messageSource.getMessage("contaCorrenteNumeroDeveSerUnico", null, null));
//        }

        ContaCorrente savedContaCorrente = contaCorrenteRepository.save(contaCorrente);

        return savedContaCorrente;
    }

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
//    public ContaCorrente update(Long id, ContaCorrente contaCorrente) throws NotFoundException, NotUniqueException {
//        ContaCorrente contaCorrenteToUpdate = findOne(id);
//
//        if (contaCorrenteToUpdate == null) {
//            throw new NotFoundException(messageSource.getMessage("contaCorrenteNaoEncontrada", null, null));
//        }
//        
//        if (!isNumberUnique(contaCorrente.getNumero(), contaCorrenteToUpdate.getId())) {
//            throw new NotUniqueException(messageSource.getMessage("contaCorrenteNumeroDeveSerUnico", null, null));
//        }
//
//        contaCorrenteToUpdate.setNome(contaCorrente.getNome());
//        contaCorrenteToUpdate.setEstado(contaCorrente.getEstado());
//
//        return contaCorrenteRepository.save(contaCorrenteToUpdate);
//    }

//    private boolean isNumberUnique(Integer numero) {
//        ContaCorrente contaCorrente = contaCorrenteRepository.findByNumero(numero);
//        
//        return contaCorrente == null ? true : false;
//    }
//    
//    private boolean isNumberUnique(Integer numero, Long id) {
//        ContaCorrente contaCorrente = contaCorrenteRepository.findByNumeroAndDifferentId(numero, id);
//        
//        return contaCorrente == null ? true : false;
//    }   
}
