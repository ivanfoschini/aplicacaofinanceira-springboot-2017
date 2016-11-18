package aplicacaofinanceira.controller;

import aplicacaofinanceira.exception.DifferentAccountsException;
import aplicacaofinanceira.exception.MoreThanOneAccountClientException;
import aplicacaofinanceira.exception.MoreThanOneAccountOwnershipException;
import aplicacaofinanceira.exception.NoAccountOwnershipException;
import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.model.Correntista;
import aplicacaofinanceira.service.CorrentistaService;
import aplicacaofinanceira.util.CorrentistaJson;
import aplicacaofinanceira.util.CorrentistaViews;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CorrentistaController extends BaseController {
 
    @Autowired
    private CorrentistaService correntistaService;
    
    @RequestMapping(
            value = "/api/correntistas",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(CorrentistaViews.CorrentistaSimple.class)
    public ResponseEntity<List<Correntista>> associate(@RequestBody List<CorrentistaJson> correntistasJson) throws DifferentAccountsException, HttpMessageNotReadableException, MoreThanOneAccountClientException, MoreThanOneAccountOwnershipException, NoAccountOwnershipException, NotFoundException {
        List<Correntista> savedCorrentistas = correntistaService.associate(correntistasJson);

        return new ResponseEntity<>(savedCorrentistas, HttpStatus.CREATED);
    }
    
    @RequestMapping(
            value = "/api/correntistas/findByCliente/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(CorrentistaViews.CorrentistaSimple.class)
    public ResponseEntity<List<Correntista>> findByCliente(@PathVariable("id") Long id) throws NotFoundException {        
        List<Correntista> correntistasByCliente = correntistaService.findByCliente(id);

        return new ResponseEntity<>(correntistasByCliente, HttpStatus.OK);        
    }
    
    @RequestMapping(
            value = "/api/correntistas/findByConta/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(CorrentistaViews.CorrentistaSimple.class)
    public ResponseEntity<List<Correntista>> findByConta(@PathVariable("id") Long id) throws NotFoundException {        
        List<Correntista> correntistasByConta = correntistaService.findByConta(id);

        return new ResponseEntity<>(correntistasByConta, HttpStatus.OK);        
    }
}