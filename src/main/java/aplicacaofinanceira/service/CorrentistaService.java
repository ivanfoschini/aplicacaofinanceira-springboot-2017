package aplicacaofinanceira.service;

import aplicacaofinanceira.exception.DifferentAccountsException;
import aplicacaofinanceira.exception.MoreThanOneAccountClientException;
import aplicacaofinanceira.exception.MoreThanOneAccountOwnershipException;
import aplicacaofinanceira.exception.NoAccountOwnershipException;
import aplicacaofinanceira.exception.NotFoundException;
import aplicacaofinanceira.model.Correntista;
import aplicacaofinanceira.util.CorrentistaJson;
import java.util.List;
import org.springframework.http.converter.HttpMessageNotReadableException;

public interface CorrentistaService {

    List<Correntista> associate(List<CorrentistaJson> correntistasJson) throws DifferentAccountsException, HttpMessageNotReadableException, MoreThanOneAccountClientException, MoreThanOneAccountOwnershipException, NoAccountOwnershipException, NotFoundException;    
}