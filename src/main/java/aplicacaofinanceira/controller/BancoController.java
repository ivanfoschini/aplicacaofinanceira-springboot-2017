package aplicacaofinanceira.controller;

import aplicacaofinanceira.model.Banco;
import aplicacaofinanceira.service.BancoService;
import aplicacaofinanceira.validation.ValidationUtil;
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
public class BancoController extends BaseController {

    @Autowired
    private BancoService bancoService;

    @RequestMapping(
            value = "/api/bancos",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createBanco(@RequestBody @Valid Banco banco, BindingResult bindingResult) {        
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(ValidationUtil.getBeanValidationErrors(bindingResult), HttpStatus.UNPROCESSABLE_ENTITY);  
        } else {
            Banco savedBanco = bancoService.insert(banco);        
        
            return new ResponseEntity<>(savedBanco, HttpStatus.CREATED);    
        }
    }
}
