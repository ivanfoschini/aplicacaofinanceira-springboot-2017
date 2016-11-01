package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.NotUniqueException;
import aplicacaofinanceira.model.Agencia;
import aplicacaofinanceira.repository.AgenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AgenciaServiceImpl implements AgenciaService {

    @Autowired
    private AgenciaRepository agenciaRepository;

    @Autowired
    private MessageSource messageSource;

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
//    public void delete(Long id) throws NotFoundException {
//        Agencia agencia = agenciaRepository.findOne(id);
//
//        if (agencia == null) {
//            throw new NotFoundException(messageSource.getMessage("agenciaNaoEncontrada", null, null));
//        }
//
//        agenciaRepository.delete(id);
//    }
//
//    @Override
//    public List<Agencia> findAll() {
//        return agenciaRepository.findAll(new Sort(Sort.Direction.ASC, "nome"));
//    }
//
//    @Override
//    public Agencia findOne(Long id) throws NotFoundException {
//        Agencia agencia = agenciaRepository.findOne(id);
//
//        if (agencia == null) {
//            throw new NotFoundException(messageSource.getMessage("agenciaNaoEncontrada", null, null));
//        }
//
//        return agencia;
//    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Agencia insert(Agencia agencia) throws NotUniqueException {
//        if (!isNomeUniqueForEstado(agencia.getNome(), agencia.getEstado().getId())) {
//            throw new NotUniqueException(messageSource.getMessage("agenciaNomeDeveSerUnicoParaEstado", null, null));
//        }

        Agencia savedAgencia = agenciaRepository.save(agencia);

        return savedAgencia;
    }

//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
//    public Agencia update(Long id, Agencia agencia) throws NotFoundException, NotUniqueException {
//        Agencia agenciaToUpdate = findOne(id);
//
//        if (agenciaToUpdate == null) {
//            throw new NoResultException(messageSource.getMessage("agenciaNaoEncontrada", null, null));
//        }
//        
//        if (!isNomeUniqueForEstado(agencia.getNome(), agencia.getEstado().getId(), agenciaToUpdate.getId())) {
//            throw new NotUniqueException(messageSource.getMessage("agenciaNomeDeveSerUnicoParaEstado", null, null));
//        }
//
//        agenciaToUpdate.setNome(agencia.getNome());
//        agenciaToUpdate.setEstado(agencia.getEstado());
//
//        return agenciaRepository.save(agenciaToUpdate);
//    }
//
//    private boolean isNomeUniqueForEstado(String nomeDaAgencia, Long idDoEstado) {        
//        Agencia agencia = agenciaRepository.findByNomeAndEstado(nomeDaAgencia, idDoEstado);
//
//        return agencia != null ? false : true;        
//    }

//    private boolean isNomeUniqueForEstado(String nomeDaAgencia, Long idDoEstadoToUpdate, Long idDaAgenciaCurrent) {
//        Agencia agencia = agenciaRepository.findByNomeAndEstadoAndDifferentId(nomeDaAgencia, idDoEstadoToUpdate, idDaAgenciaCurrent);
//
//        return agencia != null ? false : true;
//    }    
}
