package aplicacaofinanceira.service;

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
    public List<Correntista> associate(List<CorrentistaJson> correntistasJson) throws NotFoundException {
        List<Correntista> correntistas = new ArrayList<>();
        
        for (CorrentistaJson correntistaJson: correntistasJson) {
            Long contaId = correntistaJson.getContaId();
            
            Conta conta = contaRepository.findOne(contaId);            
            
            if (conta == null) {
                throw new NotFoundException(messageSource.getMessage("contaNaoEncontrada", null, null));
            }
            
            Long clienteId = correntistaJson.getClienteId();            
            
            Cliente cliente = clienteRepository.findOne(clienteId);
            
            if (cliente == null) {
                throw new NotFoundException(messageSource.getMessage("clienteNaoEncontrado", null, null));
            }
            
            Correntista correntista = new Correntista();
            correntista.setCorrentistaPK(new CorrentistaPK(contaId, clienteId));
            correntista.setConta(conta);
            correntista.setCliente(cliente);
            correntista.setTitularidade(correntistaJson.isTitularidade());
            
            correntistas.add(correntista);
        }
        
        for (Correntista correntista: correntistas) {
            correntistaRepository.save(correntista);
        }
        
        return correntistas;
    }    
}
