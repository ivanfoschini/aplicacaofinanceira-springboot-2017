package aplicacaofinanceira.service;

import aplicacaofinanceira.model.Banco;
import aplicacaofinanceira.repository.BancoRepository;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class BancoServiceImpl implements BancoService {

    @Autowired
    private BancoRepository bancoRepository;
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(Long id) {
        bancoRepository.delete(id);
    }

    @Override
    public Collection<Banco> findAll() {
        Collection<Banco> bancos = bancoRepository.findAll();

        return bancos;
    }

    @Override
    public Banco findById(Long id) {
        Banco banco = bancoRepository.findOne(id);

        return banco;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Banco insert(Banco banco) {
        if (banco.getId() != null) {
            return null;
        }

        Banco savedBanco = bancoRepository.save(banco);

        return savedBanco;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Banco update(Long id, Banco banco) {
        Banco bancoToUpdate = findById(id);
        
        if (bancoToUpdate == null) {
            return null;
        }

        bancoToUpdate.setNumero(banco.getNumero());
        bancoToUpdate.setCnpj(banco.getCnpj());
        bancoToUpdate.setNome(banco.getNome());
        
        Banco updatedBanco = bancoRepository.save(bancoToUpdate);

        return updatedBanco;
    }    
}
