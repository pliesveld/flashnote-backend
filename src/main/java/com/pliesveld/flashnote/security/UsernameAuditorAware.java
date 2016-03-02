package com.pliesveld.flashnote.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Gets the current user name of the currently authenticated user if applicable.  By implementing
 * AuditorAware we are exposing auditable entities to the Spring-Data repositories.
 */
public class UsernameAuditorAware implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated())
        {
            return null;
        }
        return ((StudentPrincipal) authentication.getPrincipal()).getUsername();
    }
}
