package aplicacaofinanceira.controller;

import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.exception.CampoUniqueException;
import aplicacaofinanceira.model.Banco;
import aplicacaofinanceira.service.BancoService;
import aplicacaofinanceira.validation.ValidationUtil;
import java.util.Collection;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
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

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(
            value = "/api/bancos/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<Banco> delete(@PathVariable("id") Long id) throws Exception {
        Banco banco = bancoService.findOne(id);

        if (banco == null) {
            throw new NotFoundException(messageSource.getMessage("bancoNotFoundError", null, null));
        }

        bancoService.delete(id);

        return new ResponseEntity<Banco>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(
            value = "/api/bancos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Banco>> findAll() {
        Collection<Banco> bancos = bancoService.findAll();

        return new ResponseEntity<Collection<Banco>>(bancos, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/api/bancos/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Banco> findOne(@PathVariable("id") Long id) throws Exception {        
        Banco banco = bancoService.findOne(id);

        if (banco == null) {
            throw new NotFoundException(messageSource.getMessage("bancoNotFoundError", null, null));
        }

        return new ResponseEntity<Banco>(banco, HttpStatus.OK);        
    }

    @RequestMapping(
            value = "/api/bancos",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> insert(@RequestBody @Valid Banco banco, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(ValidationUtil.getBeanValidationErrors(bindingResult), HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            try {
            Banco savedBanco = bancoService.insert(banco);

                if (savedBanco == null) {
                    return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
                }

                return new ResponseEntity<>(savedBanco, HttpStatus.CREATED);
            } catch (DataIntegrityViolationException e) {
                throw new CampoUniqueException(messageSource.getMessage("bancoUniqueError", null, null));
            }
        }
    }

    @RequestMapping(
            value = "/api/bancos/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> update(@PathVariable("id") Long id, @RequestBody @Valid Banco banco, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(ValidationUtil.getBeanValidationErrors(bindingResult), HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            try {
                Banco updatedBanco = bancoService.update(id, banco);

                if (updatedBanco == null) {
                    throw new NotFoundException(messageSource.getMessage("bancoNotFoundError", null, null));
                }
                
                return new ResponseEntity<>(updatedBanco, HttpStatus.OK);
            } catch (DataIntegrityViolationException e) {
                throw new CampoUniqueException(messageSource.getMessage("bancoUniqueError", null, null));
            }
        }
    }
}
