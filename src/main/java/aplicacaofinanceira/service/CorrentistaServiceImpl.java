package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.DifferentAccountsException;
import aplicacaofinanceira.exception.MoreThanOneAccountClientException;
import aplicacaofinanceira.exception.MoreThanOneAccountOwnershipException;
import aplicacaofinanceira.exception.NoAccountOwnershipException;
import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.model.Cliente;
import aplicacaofinanceira.model.Conta;
import aplicacaofinanceira.model.Correntista;
import aplicacaofinanceira.model.CorrentistaPK;
import aplicacaofinanceira.repository.ClienteRepository;
import aplicacaofinanceira.repository.ContaRepository;
import aplicacaofinanceira.repository.CorrentistaRepository;
import aplicacaofinanceira.util.CorrentistaJson;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CorrentistaServiceImpl implements CorrentistaService {

    @Autowired
    private CorrentistaRepository correntistaRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public List<Correntista> associate(List<CorrentistaJson> correntistasJson) throws DifferentAccountsException, HttpMessageNotReadableException, MoreThanOneAccountClientException, MoreThanOneAccountOwnershipException, NoAccountOwnershipException, NotFoundException {
        if (correntistasJson.isEmpty()) throw new HttpMessageNotReadableException(messageSource.getMessage("generalBadRequest", null, null));
        
        List<Correntista> correntistas = new ArrayList<>();
        Conta conta = null;

        for (CorrentistaJson correntistaJson : correntistasJson) {
            Long contaId = correntistaJson.getContaId();

            conta = contaRepository.findOne(contaId);

            if (conta == null) throw new NotFoundException(messageSource.getMessage("contaNaoEncontrada", null, null));
            
            Long clienteId = correntistaJson.getClienteId();

            Cliente cliente = clienteRepository.findOne(clienteId);

            if (cliente == null) throw new NotFoundException(messageSource.getMessage("clienteNaoEncontrado", null, null));            

            Correntista correntista = new Correntista(new CorrentistaPK(contaId, clienteId), correntistaJson.isTitularidade(), conta, cliente);
            
            correntistas.add(correntista);
        }

        if (hasMoreThanOneAccountOwnership(correntistas)) throw new MoreThanOneAccountOwnershipException(messageSource.getMessage("correntistaTitularidadeDuplicada", null, null));
        if (hasNoAccountOwnership(correntistas)) throw new NoAccountOwnershipException(messageSource.getMessage("correntistaSemTitularidade", null, null));
        if (hasDifferentAccounts(correntistas)) throw new DifferentAccountsException(messageSource.getMessage("correntistaContasDiferentes", null, null));
        if (hasDuplicatedAccountClient(correntistas)) throw new MoreThanOneAccountClientException(messageSource.getMessage("correntistaClienteDuplicado", null, null));
                
        correntistaRepository.deleteByConta(conta);

        for (Correntista correntista : correntistas) {
            correntistaRepository.save(correntista);
        }

        return correntistas;
    }

    private boolean hasDifferentAccounts(List<Correntista> correntistas) {
        List<Conta> contas = new ArrayList<>();

        for (Correntista correntista : correntistas) {
            contas.add(correntista.getConta());            
        }
        
        return contas.stream().allMatch(e -> e.equals(contas.get(0))) ? false: true;
    }
    
    private boolean hasDuplicatedAccountClient(List<Correntista> correntistas) {
        List<Cliente> clientes = new ArrayList<>();

        for (Correntista correntista : correntistas) {            
            Cliente cliente = correntista.getCliente();
            
            if (clientes.contains(cliente)) {
                return true;
            } else {
                clientes.add(cliente);
            } 
        }

        return false;
    }

    private boolean hasMoreThanOneAccountOwnership(List<Correntista> correntistas) {
        List<Cliente> titulares = new ArrayList<>();

        for (Correntista correntista : correntistas) {
            if (correntista.getTitularidade()) {
                titulares.add(correntista.getCliente());
            }
        }

        return titulares.size() > 1 ? true : false;
    }

    private boolean hasNoAccountOwnership(List<Correntista> correntistas) {
        List<Cliente> titulares = new ArrayList<>();

        for (Correntista correntista : correntistas) {
            if (correntista.getTitularidade()) {
                titulares.add(correntista.getCliente());
            }
        }

        return titulares.isEmpty() ? true : false;
    }    
}
