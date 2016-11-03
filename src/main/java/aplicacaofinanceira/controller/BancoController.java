package aplicacaofinanceira.controller;

import aplicacaofinanceira.exception.NotEmptyCollectionException;
import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.NotUniqueException;
import aplicacaofinanceira.exception.ValidationException;
import aplicacaofinanceira.model.Banco;
import aplicacaofinanceira.service.BancoService;
import aplicacaofinanceira.util.BancoViews;
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
public class BancoController extends BaseController {

    @Autowired
    private BancoService bancoService;

    @RequestMapping(
            value = "/api/bancos/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws NotEmptyCollectionException, NotFoundException {
        bancoService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(
            value = "/api/bancos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(BancoViews.BancoSimple.class)
    public ResponseEntity<List<Banco>> findAll() {
        List<Banco> bancos = bancoService.findAll();

        return new ResponseEntity<>(bancos, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/api/bancos/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(BancoViews.BancoSimple.class)
    public ResponseEntity<Banco> findOne(@PathVariable("id") Long id) throws NotFoundException {        
        Banco banco = bancoService.findOne(id);

        return new ResponseEntity<>(banco, HttpStatus.OK);        
    }

    @RequestMapping(
            value = "/api/bancos",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(BancoViews.BancoSimple.class)
    public ResponseEntity<Banco> insert(@RequestBody @Valid Banco banco, BindingResult bindingResult) throws NotUniqueException, ValidationException  {
        if (bindingResult.hasErrors()) {
            ValidationUtil.handleValidationErrors(bindingResult);
            
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } else {            
            Banco savedBanco = bancoService.insert(banco);

            return new ResponseEntity<>(savedBanco, HttpStatus.CREATED);            
        }
    }

    @RequestMapping(
            value = "/api/bancos/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(BancoViews.BancoSimple.class)
    public ResponseEntity<Banco> update(@PathVariable("id") Long id, @RequestBody @Valid Banco banco, BindingResult bindingResult) throws NotFoundException, NotUniqueException, ValidationException {
        if (bindingResult.hasErrors()) {
            ValidationUtil.handleValidationErrors(bindingResult);
            
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            Banco updatedBanco = bancoService.update(id, banco);

            return new ResponseEntity<>(updatedBanco, HttpStatus.OK);
        }
    }
}
