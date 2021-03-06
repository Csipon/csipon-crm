package com.csipon.crm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * Created by Pasha on 21.04.2017.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler successHandler;
    private final UserDetailsService userDetailsService;
    private final AccessDeniedHandler deniedHandler;
    private final AuthenticationProvider authenticationProvider;
    private final PersistentTokenRepository tokenRepository;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, AccessDeniedHandler deniedHandler, AuthenticationProvider authenticationProvider, PersistentTokenRepository tokenRepository) {
        this.userDetailsService = userDetailsService;
        this.deniedHandler = deniedHandler;
        this.authenticationProvider = authenticationProvider;
        this.tokenRepository = tokenRepository;
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider).userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry());
        http.authorizeRequests()
                .antMatchers("/forgot", "**/generate/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .failureUrl("/login?error")
                .usernameParameter("email").passwordParameter("password")
                .successHandler(successHandler)
                .permitAll()
                .and()
                .logout().logoutSuccessUrl("/login?logout").deleteCookies("JSESSIONID")
                .and()
                .exceptionHandling().accessDeniedHandler(deniedHandler)
                .and()
                .csrf()
                .and()
                .rememberMe().tokenRepository(tokenRepository)
                .tokenValiditySeconds(1209600)
        ;
    }

    @Bean
    public RedirectStrategy redirectStrategy() {
        return new DefaultRedirectStrategy();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
