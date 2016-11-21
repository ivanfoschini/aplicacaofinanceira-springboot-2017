package aplicacaofinanceira.service;

import aplicacaofinanceira.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import aplicacaofinanceira.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Override
    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }
}