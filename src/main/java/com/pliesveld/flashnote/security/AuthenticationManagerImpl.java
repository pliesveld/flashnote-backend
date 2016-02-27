package com.pliesveld.flashnote.security;

import org.apache.logging.log4j.LogManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
class AuthenticationManagerImpl implements AuthenticationManager {
    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            LOG.debug(authentication.getPrincipal());
            LOG.debug(authentication.getPrincipal().getClass().getName());
        } catch (NullPointerException e) {
        }

        try {
            LOG.debug(authentication.getCredentials());
            LOG.debug(authentication.getCredentials().getClass().getName());
        } catch (NullPointerException e) {
        }

        try {
            LOG.debug(authentication.getDetails());
            LOG.debug(authentication.getDetails().getClass().getName());
        } catch (NullPointerException e) {
        }

        Collection<? extends GrantedAuthority> auth = authentication.getAuthorities();
        auth.forEach((authority) -> LOG.debug("auth {}",authority));
        // throw new DisabledException if acount disabled
        // throw new LockedException if account is locked
        // trhow new BadCredentialsException if incorrect credentials are presented

        return authentication;
    }
}
