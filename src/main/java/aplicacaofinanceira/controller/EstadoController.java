package aplicacaofinanceira.controller;

import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.NotUniqueException;
import aplicacaofinanceira.exception.ValidationException;
import aplicacaofinanceira.model.Estado;
import aplicacaofinanceira.service.EstadoService;
import aplicacaofinanceira.util.EstadoViews;
import aplicacaofinanceira.validation.ValidationUtil;
import com.fasterxml.jackson.annotation.JsonView;
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
public class EstadoController extends BaseController {

    @Autowired
    private EstadoService estadoService;

    @RequestMapping(
            value = "/api/estados/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws NotFoundException {
        estadoService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(
            value = "/api/estados",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(EstadoViews.EstadoSimple.class)
    public ResponseEntity<List<Estado>> findAll() {
        List<Estado> estados = estadoService.findAll();

        return new ResponseEntity<>(estados, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/api/estados/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(EstadoViews.EstadoSimple.class)
    public ResponseEntity<Estado> findOne(@PathVariable("id") Long id) throws NotFoundException {        
        Estado estado = estadoService.findOne(id);

        return new ResponseEntity<>(estado, HttpStatus.OK);        
    }

    @RequestMapping(
            value = "/api/estados",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(EstadoViews.EstadoSimple.class)
    public ResponseEntity<Estado> insert(@RequestBody @Valid Estado estado, BindingResult bindingResult) throws NotUniqueException, ValidationException  {
        if (bindingResult.hasErrors()) {
            ValidationUtil.handleValidationErrors(bindingResult);
            
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } else {            
            Estado savedEstado = estadoService.insert(estado);

            return new ResponseEntity<>(savedEstado, HttpStatus.CREATED);            
        }
    }

    @RequestMapping(
            value = "/api/estados/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(EstadoViews.EstadoSimple.class)
    public ResponseEntity<Estado> update(@PathVariable("id") Long id, @RequestBody @Valid Estado estado, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException {
        if (bindingResult.hasErrors()) {
            ValidationUtil.handleValidationErrors(bindingResult);
            
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            Estado updatedEstado = estadoService.update(id, estado);

            return new ResponseEntity<>(updatedEstado, HttpStatus.OK);
        }
    }    
}