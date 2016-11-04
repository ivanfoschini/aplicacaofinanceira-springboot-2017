package aplicacaofinanceira.controller;

import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.NotUniqueException;
import aplicacaofinanceira.exception.ValidationException;
import aplicacaofinanceira.model.Agencia;
import aplicacaofinanceira.model.ContaCorrente;
import aplicacaofinanceira.service.AgenciaService;
import aplicacaofinanceira.service.ContaCorrenteService;
import aplicacaofinanceira.util.ContaCorrenteWithAgenciaSerializer;
import aplicacaofinanceira.validation.ValidationUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContaCorrenteController extends BaseController {

    @Autowired
    private ContaCorrenteService contaCorrenteService;
    
    @Autowired
    private AgenciaService agenciaService;

//    @RequestMapping(
//            value = "/api/contasCorrentes/{id}",
//            method = RequestMethod.DELETE)
//    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws NotFoundException {
//        contaCorrenteService.delete(id);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    @RequestMapping(
//            value = "/api/contasCorrentes",
//            method = RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    @JsonView(ContaCorrenteViews.ContaCorrenteSimple.class)        
//    public ResponseEntity<List<ContaCorrente>> findAll() {
//        List<ContaCorrente> contasCorrentes = contaCorrenteService.findAll();
//
//        return new ResponseEntity<>(contasCorrentes, HttpStatus.OK);
//    }
//
//    @RequestMapping(
//            value = "/api/contasCorrentes/{id}",
//            method = RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    @JsonView(ContaCorrenteViews.ContaCorrenteSimple.class)    
//    public ResponseEntity<ContaCorrente> findOne(@PathVariable("id") Long id) throws NotFoundException {        
//        ContaCorrente contaCorrente = contaCorrenteService.findOne(id);
//
//        return new ResponseEntity<>(contaCorrente, HttpStatus.OK);        
//    }

    @RequestMapping(
            value = "/api/contasCorrentes",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> insert(@RequestBody @Valid ContaCorrente contaCorrente, BindingResult bindingResult) throws JsonProcessingException, NotFoundException, NotUniqueException, ValidationException {
        if (bindingResult.hasErrors()) {
            ValidationUtil.handleValidationErrors(bindingResult);
            
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } else {            
            ContaCorrente savedContaCorrente = contaCorrenteService.insert(contaCorrente);   

            Agencia agencia = agenciaService.findOne(savedContaCorrente.getAgencia().getId());
                        
            return new ResponseEntity<>(ContaCorrenteWithAgenciaSerializer.serializeContaCorrenteWithAgencia(savedContaCorrente, agencia), HttpStatus.CREATED);            
        }
    }

//    @RequestMapping(
//            value = "/api/contasCorrentes/{id}",
//            method = RequestMethod.PUT,
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody @Valid ContaCorrente contaCorrente, BindingResult bindingResult) throws JsonProcessingException, NotFoundException, NotUniqueException, ValidationException {
//        if (bindingResult.hasErrors()) {
//            ValidationUtil.handleValidationErrors(bindingResult);
//            
//            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
//        } else {
//            ContaCorrente updatedContaCorrente = contaCorrenteService.update(id, contaCorrente);
//            
//            Agencia agencia = agenciaService.findOne(updatedContaCorrente.getAgencia().getId());
//
//            return new ResponseEntity<>(ContaCorrenteWithAgenciaSerializer.serializeContaCorrenteWithAgencia(updatedContaCorrente, agencia), HttpStatus.OK);
//        }
//    }
}
