package aplicacaofinanceira.controller;

import aplicacaofinanceira.exception.EmptyCollectionException;
import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.ValidationException;
import aplicacaofinanceira.model.ClientePessoaFisica;
import aplicacaofinanceira.model.Endereco;
import aplicacaofinanceira.service.ClientePessoaFisicaService;
import aplicacaofinanceira.util.ClientePessoaFisicaSerializer;
import aplicacaofinanceira.util.ClientePessoaFisicaViews;
import aplicacaofinanceira.validation.ValidationUtil;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.Collection;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientePessoaFisicaController extends BaseController {

    @Autowired
    private ClientePessoaFisicaService clientePessoaFisicaService;
    

//    @RequestMapping(
//            value = "/api/clientesPessoasFisicas/{id}",
//            method = RequestMethod.DELETE)
//    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws NotFoundException {
//        clientePessoaFisicaService.delete(id);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

    @RequestMapping(
            value = "/api/clientesPessoasFisicas",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(ClientePessoaFisicaViews.ClientePessoaFisicaSimple.class)        
    public ResponseEntity<List<ClientePessoaFisica>> findAll() {
        List<ClientePessoaFisica> clientesPessoasFisicas = clientePessoaFisicaService.findAll();

        return new ResponseEntity<>(clientesPessoasFisicas, HttpStatus.OK);
    }

//    @RequestMapping(
//            value = "/api/clientesPessoasFisicas/{id}",
//            method = RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    @JsonView(ClientePessoaFisicaViews.ClientePessoaFisicaSimple.class)    
//    public ResponseEntity<ClientePessoaFisica> findOne(@PathVariable("id") Long id) throws NotFoundException {        
//        ClientePessoaFisica clientePessoaFisica = clientePessoaFisicaService.findOne(id);
//
//        return new ResponseEntity<>(clientePessoaFisica, HttpStatus.OK);        
//    }

    @RequestMapping(
            value = "/api/clientesPessoasFisicas",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> insert(@RequestBody @Valid ClientePessoaFisica clientePessoaFisica, BindingResult bindingResultClientePessoaFisica) throws EmptyCollectionException, JsonProcessingException, NotFoundException, ValidationException {
        List<FieldError> enderecosFieldErrors = validateEnderecos(clientePessoaFisica.getEnderecos());
        
        if (bindingResultClientePessoaFisica.hasErrors() || !enderecosFieldErrors.isEmpty()) {
            List<FieldError> fieldErrors = new ArrayList<>(); 
            
            fieldErrors.addAll(bindingResultClientePessoaFisica.getFieldErrors());
            fieldErrors.addAll(enderecosFieldErrors);
            
            ValidationUtil.handleValidationErrors(fieldErrors);
            
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } else {            
            ClientePessoaFisica savedClientePessoaFisica = clientePessoaFisicaService.insert(clientePessoaFisica);   

            return new ResponseEntity<>(ClientePessoaFisicaSerializer.serializeClientePessoaFisica(savedClientePessoaFisica), HttpStatus.CREATED);
        }
    }

//    @RequestMapping(
//            value = "/api/clientesPessoasFisicas/{id}",
//            method = RequestMethod.PUT,
//            consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody @Valid ClientePessoaFisica clientePessoaFisica, BindingResult bindingResult) throws EmptyCollectionException, JsonProcessingException, NotFoundException, ValidationException {
//        if (bindingResult.hasErrors()) {
//            ValidationUtil.handleValidationErrors(bindingResult);
//            
//            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
//        } else {
//            ClientePessoaFisica updatedClientePessoaFisica = clientePessoaFisicaService.update(id, clientePessoaFisica);
//            
//            Agencia agencia = agenciaService.findOne(updatedClientePessoaFisica.getAgencia().getId());
//
//            return new ResponseEntity<>(ClientePessoaFisicaWithAgenciaSerializer.serializeClientePessoaFisicaWithAgencia(updatedClientePessoaFisica, agencia), HttpStatus.OK);
//        }
//    }
    
    private List<FieldError> validateEnderecos(Collection<Endereco> enderecos) {
        List<FieldError> enderecoFieldErrors = new ArrayList<>();  
        
        for (Endereco endereco: enderecos) {
            Set<ConstraintViolation<Endereco>> enderecoViolations = Validation.buildDefaultValidatorFactory().getValidator().validate(endereco);
        
            for (ConstraintViolation<Endereco> violation: enderecoViolations) {
                FieldError enderecoFieldError = new FieldError("endereco", "field", violation.getMessage());

                enderecoFieldErrors.add(enderecoFieldError);
            }
        }
        
        return enderecoFieldErrors;
    }
}