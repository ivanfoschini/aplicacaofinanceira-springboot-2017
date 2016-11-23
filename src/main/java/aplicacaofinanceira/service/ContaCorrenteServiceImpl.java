package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.NotEmptyCollectionException;
import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.NotUniqueException;
import aplicacaofinanceira.model.Conta;
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
    public void delete(Long id) throws NotEmptyCollectionException, NotFoundException {
        ContaCorrente contaCorrente = contaCorrenteRepository.findOne(id);

        if (contaCorrente == null) {
            throw new NotFoundException(messageSource.getMessage("contaNaoEncontrada", null, null));
        }
        
        if (!contaCorrente.getCorrentistas().isEmpty()) {
            throw new NotEmptyCollectionException(messageSource.getMessage("contaPossuiCorrentista", null, null));
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
            throw new NotFoundException(messageSource.getMessage("contaNaoEncontrada", null, null));
        }

        return contaCorrente;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ContaCorrente insert(ContaCorrente contaCorrente) throws NotUniqueException {
        if (!isNumberUnique(contaCorrente.getNumero())) {
            throw new NotUniqueException(messageSource.getMessage("contaNumeroDeveSerUnico", null, null));
        }

        ContaCorrente savedContaCorrente = contaCorrenteRepository.save(contaCorrente);

        return savedContaCorrente;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ContaCorrente update(Long id, ContaCorrente contaCorrente) throws NotFoundException, NotUniqueException {
        ContaCorrente contaCorrenteToUpdate = findOne(id);

        if (contaCorrenteToUpdate == null) {
            throw new NotFoundException(messageSource.getMessage("contaNaoEncontrada", null, null));
        }
        
        if (!isNumberUnique(contaCorrente.getNumero(), contaCorrenteToUpdate.getId())) {
            throw new NotUniqueException(messageSource.getMessage("contaNumeroDeveSerUnico", null, null));
        }

        contaCorrenteToUpdate.setNumero(contaCorrente.getNumero());
        contaCorrenteToUpdate.setSaldo(contaCorrente.getSaldo());
        contaCorrenteToUpdate.setDataDeAbertura(contaCorrente.getDataDeAbertura());
        contaCorrenteToUpdate.setLimite(contaCorrente.getLimite());
        contaCorrenteToUpdate.setAgencia(contaCorrente.getAgencia());

        return contaCorrenteRepository.save(contaCorrenteToUpdate);
    }

    private boolean isNumberUnique(Integer numero) {
        Conta conta = contaCorrenteRepository.findByNumero(numero);
        
        return conta == null ? true : false;
    }
    
    private boolean isNumberUnique(Integer numero, Long id) {
        Conta conta = contaCorrenteRepository.findByNumeroAndDifferentId(numero, id);
        
        return conta == null ? true : false;
    }   
}
