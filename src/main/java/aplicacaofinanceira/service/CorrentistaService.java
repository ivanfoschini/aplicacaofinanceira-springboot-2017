package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.model.Correntista;
import aplicacaofinanceira.util.CorrentistaJson;
import java.util.List;

public interface CorrentistaService {

    List<Correntista> associate(List<CorrentistaJson> correntistasJson) throws NotFoundException;    
}