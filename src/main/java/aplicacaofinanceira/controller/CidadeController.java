package aplicacaofinanceira.controller;

import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.NotUniqueException;
import aplicacaofinanceira.exception.ValidationException;
import aplicacaofinanceira.model.Cidade;
import aplicacaofinanceira.model.Estado;
import aplicacaofinanceira.service.CidadeService;
import aplicacaofinanceira.service.EstadoService;
import aplicacaofinanceira.util.CidadeSerializer;
import aplicacaofinanceira.validation.ValidationUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Collection;
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
public class CidadeController extends BaseController {

    @Autowired
    private CidadeService cidadeService;
    
    @Autowired
    private EstadoService estadoService;

    @RequestMapping(
            value = "/api/cidades/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Cidade> delete(@PathVariable("id") Long id) throws NotFoundException {
        cidadeService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(
            value = "/api/cidades",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Cidade>> findAll() {
        Collection<Cidade> cidades = cidadeService.findAll();

        return new ResponseEntity<>(cidades, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/api/cidades/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cidade> findOne(@PathVariable("id") Long id) throws NotFoundException {        
        Cidade cidade = cidadeService.findOne(id);

        return new ResponseEntity<>(cidade, HttpStatus.OK);        
    }

    @RequestMapping(
            value = "/api/cidades",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> insert(@RequestBody @Valid Cidade cidade, BindingResult bindingResult) throws NotUniqueException, ValidationException, NotFoundException, JsonProcessingException{
        if (bindingResult.hasErrors()) {
            ValidationUtil.handleValidationErrors(bindingResult);
            
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } else {            
            Cidade savedCidade = cidadeService.insert(cidade);   

            Estado estado = estadoService.findOne(savedCidade.getEstado().getId());
                        
            return new ResponseEntity<>(CidadeSerializer.serialize(savedCidade, estado), HttpStatus.CREATED);            
        }
    }

    @RequestMapping(
            value = "/api/cidades/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody @Valid Cidade cidade, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException {
        if (bindingResult.hasErrors()) {
            ValidationUtil.handleValidationErrors(bindingResult);
            
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            Cidade updatedCidade = cidadeService.update(id, cidade);

            return new ResponseEntity<>(updatedCidade, HttpStatus.OK);
        }
    }    
}