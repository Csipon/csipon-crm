package com.csipon.crm.security;

import com.csipon.crm.domain.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by Pasha on 21.04.2017.
 */
@Component(value = "successHandler")
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    private final RedirectStrategy redirectStrategy;

    @Autowired
    public AuthenticationSuccessHandlerImpl(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws IOException {
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    protected void handle(HttpServletRequest request,
                          HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(authentication);
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }


    protected String determineTargetUrl(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (hasAnyRole(grantedAuthority)) {
                return "/";
            }
        }
        throw new IllegalStateException();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    private boolean hasAnyRole(GrantedAuthority grantedAuthority) {
        return UserRole.ROLE_ADMIN == UserRole.valueOf(grantedAuthority.getAuthority()) || UserRole.ROLE_CSR == UserRole.valueOf(grantedAuthority.getAuthority())
                || UserRole.ROLE_CUSTOMER == UserRole.valueOf(grantedAuthority.getAuthority()) || UserRole.ROLE_PMG == UserRole.valueOf(grantedAuthority.getAuthority());
    }
}
