package aplicacaofinanceira;

import aplicacaofinanceira.security.AccountAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private AccountAuthenticationProvider accountAuthenticationProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(accountAuthenticationProvider);
    }

    @Configuration
    public static class ApiWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http
                .authorizeRequests()
                    .antMatchers("/api/agencias").hasRole("ADMIN")
                    .antMatchers("/api/bancos").hasRole("ADMIN")
                    .antMatchers("/api/cidades").hasRole("ADMIN")
                    .antMatchers("/api/cidades").hasRole("ADMIN")
                    .antMatchers("/api/clientesPessoasFisicas").hasAnyRole("ADMIN", "FUNCIONARIO")
                    .antMatchers("/api/clientesPessoasJuridicas").hasAnyRole("ADMIN", "FUNCIONARIO")
                    .antMatchers("/api/contasCorrentes").hasAnyRole("ADMIN", "FUNCIONARIO")
                    .antMatchers("/api/contasPoupancas").hasAnyRole("ADMIN", "FUNCIONARIO")
                    .antMatchers("/api/correntistas").hasAnyRole("ADMIN", "FUNCIONARIO")
                    .antMatchers("/api/estados").hasRole("ADMIN")
                .and()
                    .httpBasic()
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .csrf()
                    .disable();
        }
    }
}
