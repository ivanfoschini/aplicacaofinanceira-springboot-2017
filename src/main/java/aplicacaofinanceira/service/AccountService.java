package aplicacaofinanceira.service;

import aplicacaofinanceira.model.Account;

public interface AccountService {
    
    Account findByUsername(String username);
}