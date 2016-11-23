package aplicacaofinanceira.controller;

import aplicacaofinanceira.exception.NotEmptyCollectionException;
import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.NotUniqueException;
import aplicacaofinanceira.exception.ValidationException;
import aplicacaofinanceira.model.Agencia;
import aplicacaofinanceira.model.ContaPoupanca;
import aplicacaofinanceira.service.AgenciaService;
import aplicacaofinanceira.service.ContaPoupancaService;
import aplicacaofinanceira.util.ContaPoupancaViews;
import aplicacaofinanceira.util.ContaPoupancaWithAgenciaSerializer;
import aplicacaofinanceira.validation.ValidationUtil;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContaPoupancaController extends BaseController {

    @Autowired
    private ContaPoupancaService contaPoupancaService;
    
    @Autowired
    private AgenciaService agenciaService;

    @RequestMapping(
            value = "/api/contasPoupancas/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws NotEmptyCollectionException, NotFoundException {
        contaPoupancaService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(
            value = "/api/contasPoupancas",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(ContaPoupancaViews.ContaPoupancaSimple.class)        
    public ResponseEntity<List<ContaPoupanca>> findAll() {
        List<ContaPoupanca> contasPoupancas = contaPoupancaService.findAll();

        return new ResponseEntity<>(contasPoupancas, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/api/contasPoupancas/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(ContaPoupancaViews.ContaPoupancaSimple.class)    
    public ResponseEntity<ContaPoupanca> findOne(@PathVariable("id") Long id) throws NotFoundException {        
        ContaPoupanca contaPoupanca = contaPoupancaService.findOne(id);

        return new ResponseEntity<>(contaPoupanca, HttpStatus.OK);        
    }

    @RequestMapping(
            value = "/api/contasPoupancas",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> insert(@RequestBody @Valid ContaPoupanca contaPoupanca, BindingResult bindingResult) throws JsonProcessingException, NotFoundException, NotUniqueException, ValidationException {
        if (bindingResult.hasErrors()) {
            ValidationUtil.handleValidationErrors(bindingResult);
            
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } else {            
            ContaPoupanca savedContaPoupanca = contaPoupancaService.insert(contaPoupanca);   

            Agencia agencia = agenciaService.findOne(savedContaPoupanca.getAgencia().getId());
                        
            return new ResponseEntity<>(ContaPoupancaWithAgenciaSerializer.serializeContaPoupancaWithAgencia(savedContaPoupanca, agencia), HttpStatus.CREATED);            
        }
    }

    @RequestMapping(
            value = "/api/contasPoupancas/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody @Valid ContaPoupanca contaPoupanca, BindingResult bindingResult) throws JsonProcessingException, NotFoundException, NotUniqueException, ValidationException {
        if (bindingResult.hasErrors()) {
            ValidationUtil.handleValidationErrors(bindingResult);
            
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            ContaPoupanca updatedContaPoupanca = contaPoupancaService.update(id, contaPoupanca);
            
            Agencia agencia = agenciaService.findOne(updatedContaPoupanca.getAgencia().getId());

            return new ResponseEntity<>(ContaPoupancaWithAgenciaSerializer.serializeContaPoupancaWithAgencia(updatedContaPoupanca, agencia), HttpStatus.OK);
        }
    }    
}
