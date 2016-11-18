package aplicacaofinanceira.controller;

import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.model.Correntista;
import aplicacaofinanceira.service.CorrentistaService;
import aplicacaofinanceira.util.CorrentistaJson;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
//    @JsonView(BancoViews.BancoSimple.class)
//    public ResponseEntity<Object> associate(@RequestBody @Valid List<Correntista> correntistas, BindingResult bindingResult) throws ValidationException  {
    public ResponseEntity<Object> associate(@RequestBody List<CorrentistaJson> correntistasJson) throws NotFoundException {
//        if (bindingResult.hasErrors()) {
//            ValidationUtil.handleValidationErrors(bindingResult);
//            
//            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
//        } else {            
            List<Correntista> savedCorrentistas = correntistaService.associate(correntistasJson);

            return new ResponseEntity<>(savedCorrentistas, HttpStatus.CREATED);            
//        }
    }
}