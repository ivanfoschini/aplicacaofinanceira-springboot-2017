package aplicacaofinanceira.controller;

import aplicacaofinanceira.exception.NotEmptyCollectionException;
import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.NotUniqueException;
import aplicacaofinanceira.exception.ValidationException;
import aplicacaofinanceira.model.Agencia;
import aplicacaofinanceira.model.Banco;
import aplicacaofinanceira.model.Cidade;
import aplicacaofinanceira.model.Endereco;
import aplicacaofinanceira.model.Estado;
import aplicacaofinanceira.service.AgenciaService;
import aplicacaofinanceira.service.BancoService;
import aplicacaofinanceira.service.CidadeService;
import aplicacaofinanceira.service.EstadoService;
import aplicacaofinanceira.util.AgenciaViews;
import aplicacaofinanceira.util.AgenciaWithEnderecoAndBancoSerializer;
import aplicacaofinanceira.util.ContaCorrenteViews;
import aplicacaofinanceira.util.HibernateUtil;
import aplicacaofinanceira.validation.ValidationUtil;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgenciaController extends BaseController {

    @Autowired
    private AgenciaService agenciaService;
    
    @Autowired
    private BancoService bancoService;
    
    @Autowired
    private CidadeService cidadeService;
    
    @Autowired
    private EstadoService estadoService;

    @RequestMapping(
            value = "/api/agencias/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws NotEmptyCollectionException, NotFoundException {
        agenciaService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(
            value = "/api/agencias",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(AgenciaViews.AgenciaSimple.class)        
    public ResponseEntity<List<Agencia>> findAll() {
        List<Agencia> agencias = agenciaService.findAll();

        return new ResponseEntity<>(agencias, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/api/agencias/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(ContaCorrenteViews.ContaCorrenteSimple.class)    
    public ResponseEntity<Agencia> findOne(@PathVariable("id") Long id) throws NotFoundException {        
        Agencia agencia = agenciaService.findOne(id);

        return new ResponseEntity<>(agencia, HttpStatus.OK);        
    }

    @RequestMapping(
            value = "/api/agencias",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> insert(@RequestBody @Valid Agencia agencia, BindingResult bindingResultAgencia) throws JsonProcessingException, NotFoundException, NotUniqueException, ValidationException {
        List<FieldError> enderecoFieldErrors = validateEndereco(agencia.getEndereco());
        
        if (bindingResultAgencia.hasErrors() || !enderecoFieldErrors.isEmpty()) {
            List<FieldError> fieldErrors = new ArrayList<>(); 
            
            fieldErrors.addAll(bindingResultAgencia.getFieldErrors());
            fieldErrors.addAll(enderecoFieldErrors);
            
            ValidationUtil.handleValidationErrors(fieldErrors);
            
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } else {            
            Agencia savedAgencia = agenciaService.insert(agencia);   

            Cidade cidade = cidadeService.findOne(savedAgencia.getEndereco().getCidade().getId());
            Estado estado = HibernateUtil.initializeAndUnproxy(estadoService.findOne(cidade.getEstado().getId()));
            Banco banco = bancoService.findOne(savedAgencia.getBanco().getId());            
                        
            return new ResponseEntity<>(AgenciaWithEnderecoAndBancoSerializer.serializeAgenciaWithEnderecoAndBanco(savedAgencia, cidade, estado, banco), HttpStatus.CREATED);            
        }
    }    
    
    @RequestMapping(
            value = "/api/agencias/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody @Valid Agencia agencia, BindingResult bindingResultAgencia) throws JsonProcessingException, NotFoundException, NotUniqueException, ValidationException {
        List<FieldError> enderecoFieldErrors = validateEndereco(agencia.getEndereco());
        
        if (bindingResultAgencia.hasErrors() || !enderecoFieldErrors.isEmpty()) {
            List<FieldError> fieldErrors = new ArrayList<>(); 
            
            fieldErrors.addAll(bindingResultAgencia.getFieldErrors());
            fieldErrors.addAll(enderecoFieldErrors);
            
            ValidationUtil.handleValidationErrors(fieldErrors);
            
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            Agencia updatedAgencia = agenciaService.update(id, agencia);
            
            Cidade cidade = cidadeService.findOne(updatedAgencia.getEndereco().getCidade().getId());
            Estado estado = HibernateUtil.initializeAndUnproxy(estadoService.findOne(cidade.getEstado().getId()));
            Banco banco = bancoService.findOne(updatedAgencia.getBanco().getId());            

            return new ResponseEntity<>(AgenciaWithEnderecoAndBancoSerializer.serializeAgenciaWithEnderecoAndBanco(updatedAgencia, cidade, estado, banco), HttpStatus.OK);
        }
    }      

    private List<FieldError> validateEndereco(Endereco endereco) {
        Set<ConstraintViolation<Endereco>> enderecoViolations = Validation.buildDefaultValidatorFactory().getValidator().validate(endereco);
        
        List<FieldError> enderecoFieldErrors = new ArrayList<>();  
        
        for (ConstraintViolation<Endereco> violation: enderecoViolations) {
            FieldError enderecoFieldError = new FieldError("endereco", "field", violation.getMessage());
            
            enderecoFieldErrors.add(enderecoFieldError);
        }
        
        return enderecoFieldErrors;
    }
}
