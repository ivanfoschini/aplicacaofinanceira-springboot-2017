package aplicacaofinanceira.security;

import aplicacaofinanceira.model.Account;
import aplicacaofinanceira.model.Role;
import aplicacaofinanceira.service.AccountService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AccountUserDetailsServiceImpl implements UserDetailsService {
 
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private MessageSource messageSource;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountService.findByUsername(username);
        
        if (account == null) {
            throw new UsernameNotFoundException(messageSource.getMessage("usuarioNaoEncontrado", null, null));
        }

        if (account.getRoles() == null || account.getRoles().isEmpty()) {
            throw new UsernameNotFoundException(messageSource.getMessage("usuarioNaoAutorizado", null, null));
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        
        for (Role role : account.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getCode()));
        }

        User userDetails = new User(account.getUsername(),
                account.getPassword(), account.isEnabled(),
                !account.isExpired(), !account.isCredentialsExpired(),
                !account.isLocked(), grantedAuthorities);

        return userDetails;
    }
}