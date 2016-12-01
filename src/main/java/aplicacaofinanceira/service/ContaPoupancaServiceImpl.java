package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.NotUniqueException;
import aplicacaofinanceira.model.Agencia;
import aplicacaofinanceira.model.Conta;
import aplicacaofinanceira.model.ContaPoupanca;
import aplicacaofinanceira.repository.AgenciaRepository;
import aplicacaofinanceira.repository.ContaPoupancaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ContaPoupancaServiceImpl implements ContaPoupancaService {

    @Autowired
    private AgenciaRepository agenciaRepository;
    
    @Autowired
    private ContaPoupancaRepository contaPoupancaRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(Long id) throws NotFoundException {
        ContaPoupanca contaPoupanca = contaPoupancaRepository.findOne(id);

        if (contaPoupanca == null) {
            throw new NotFoundException(messageSource.getMessage("contaNaoEncontrada", null, null));
        }

        contaPoupancaRepository.delete(contaPoupanca);
    }

    @Override
    public List<ContaPoupanca> findAll() {
        return contaPoupancaRepository.findAll(new Sort(Sort.Direction.ASC, "numero"));
    }

    @Override
    public ContaPoupanca findOne(Long id) throws NotFoundException {
        ContaPoupanca contaPoupanca = contaPoupancaRepository.findOne(id);

        if (contaPoupanca == null) {
            throw new NotFoundException(messageSource.getMessage("contaNaoEncontrada", null, null));
        }

        return contaPoupanca;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ContaPoupanca insert(ContaPoupanca contaPoupanca) throws NotFoundException, NotUniqueException {
        validateAgencia(contaPoupanca);
        
        if (!isNumberUnique(contaPoupanca.getNumero())) {
            throw new NotUniqueException(messageSource.getMessage("contaNumeroDeveSerUnico", null, null));
        }

        return contaPoupancaRepository.save(contaPoupanca);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ContaPoupanca update(Long id, ContaPoupanca contaPoupanca) throws NotFoundException, NotUniqueException {
        validateAgencia(contaPoupanca);
        
        ContaPoupanca contaPoupancaToUpdate = findOne(id);

        if (contaPoupancaToUpdate == null) {
            throw new NotFoundException(messageSource.getMessage("contaNaoEncontrada", null, null));
        }
        
        if (!isNumberUnique(contaPoupanca.getNumero(), contaPoupancaToUpdate.getId())) {
            throw new NotUniqueException(messageSource.getMessage("contaNumeroDeveSerUnico", null, null));
        }

        contaPoupancaToUpdate.setNumero(contaPoupanca.getNumero());
        contaPoupancaToUpdate.setSaldo(contaPoupanca.getSaldo());
        contaPoupancaToUpdate.setDataDeAbertura(contaPoupanca.getDataDeAbertura());
        contaPoupancaToUpdate.setDataDeAniversario(contaPoupanca.getDataDeAniversario());
        contaPoupancaToUpdate.setCorrecaoMonetaria(contaPoupanca.getCorrecaoMonetaria());
        contaPoupancaToUpdate.setJuros(contaPoupanca.getJuros());
        contaPoupancaToUpdate.setAgencia(contaPoupanca.getAgencia());

        return contaPoupancaRepository.save(contaPoupancaToUpdate);
    }

    private boolean isNumberUnique(Integer numero) {
        Conta conta = contaPoupancaRepository.findByNumero(numero);
        
        return conta == null ? true : false;
    }
    
    private boolean isNumberUnique(Integer numero, Long id) {
        Conta conta = contaPoupancaRepository.findByNumeroAndDifferentId(numero, id);
        
        return conta == null ? true : false;
    }
    
    private void validateAgencia(ContaPoupanca contaPoupanca) throws NotFoundException {
        Agencia agencia = agenciaRepository.findOne(contaPoupanca.getAgencia().getId());
        
        if (agencia == null) {
            throw new NotFoundException(messageSource.getMessage("agenciaNaoEncontrada", null, null));
        }
    }
}