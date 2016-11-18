package aplicacaofinanceira.controller;

import aplicacaofinanceira.exception.EmptyCollectionException;
import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.ValidationException;
import aplicacaofinanceira.model.ClientePessoaJuridica;
import aplicacaofinanceira.model.Endereco;
import aplicacaofinanceira.service.ClientePessoaJuridicaService;
import aplicacaofinanceira.util.ClientePessoaJuridicaSerializer;
import aplicacaofinanceira.util.ClientePessoaJuridicaViews;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientePessoaJuridicaController extends BaseController {

    @Autowired
    private ClientePessoaJuridicaService clientePessoaJuridicaService;    

    @RequestMapping(
            value = "/api/clientesPessoasJuridicas/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws NotFoundException {
        clientePessoaJuridicaService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(
            value = "/api/clientesPessoasJuridicas",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(ClientePessoaJuridicaViews.ClientePessoaJuridicaSimple.class)        
    public ResponseEntity<List<ClientePessoaJuridica>> findAll() {
        List<ClientePessoaJuridica> clientesPessoasJuridicas = clientePessoaJuridicaService.findAll();

        return new ResponseEntity<>(clientesPessoasJuridicas, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/api/clientesPessoasJuridicas/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(ClientePessoaJuridicaViews.ClientePessoaJuridicaSimple.class)    
    public ResponseEntity<ClientePessoaJuridica> findOne(@PathVariable("id") Long id) throws NotFoundException {        
        ClientePessoaJuridica clientePessoaJuridica = clientePessoaJuridicaService.findOne(id);

        return new ResponseEntity<>(clientePessoaJuridica, HttpStatus.OK);        
    }

    @RequestMapping(
            value = "/api/clientesPessoasJuridicas",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> insert(@RequestBody @Valid ClientePessoaJuridica clientePessoaJuridica, BindingResult bindingResultClientePessoaJuridica) throws EmptyCollectionException, JsonProcessingException, NotFoundException, ValidationException {
        List<FieldError> enderecosFieldErrors = validateEnderecos(clientePessoaJuridica.getEnderecos());
        
        if (bindingResultClientePessoaJuridica.hasErrors() || !enderecosFieldErrors.isEmpty()) {
            List<FieldError> fieldErrors = new ArrayList<>(); 
            
            fieldErrors.addAll(bindingResultClientePessoaJuridica.getFieldErrors());
            fieldErrors.addAll(enderecosFieldErrors);
            
            ValidationUtil.handleValidationErrors(fieldErrors);
            
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } else {            
            ClientePessoaJuridica savedClientePessoaJuridica = clientePessoaJuridicaService.insert(clientePessoaJuridica);   

            return new ResponseEntity<>(ClientePessoaJuridicaSerializer.serializeClientePessoaJuridica(savedClientePessoaJuridica), HttpStatus.CREATED);
        }
    }

    @RequestMapping(
            value = "/api/clientesPessoasJuridicas/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody @Valid ClientePessoaJuridica clientePessoaJuridica, BindingResult bindingResultClientePessoaJuridica) throws EmptyCollectionException, JsonProcessingException, NotFoundException, ValidationException {
        List<FieldError> enderecosFieldErrors = validateEnderecos(clientePessoaJuridica.getEnderecos());
        
        if (bindingResultClientePessoaJuridica.hasErrors() || !enderecosFieldErrors.isEmpty()) {
            List<FieldError> fieldErrors = new ArrayList<>(); 
            
            fieldErrors.addAll(bindingResultClientePessoaJuridica.getFieldErrors());
            fieldErrors.addAll(enderecosFieldErrors);
            
            ValidationUtil.handleValidationErrors(fieldErrors);
            
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            ClientePessoaJuridica updatedClientePessoaJuridica = clientePessoaJuridicaService.update(id, clientePessoaJuridica);
            
            return new ResponseEntity<>(ClientePessoaJuridicaSerializer.serializeClientePessoaJuridica(updatedClientePessoaJuridica), HttpStatus.OK);
        }
    }
    
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