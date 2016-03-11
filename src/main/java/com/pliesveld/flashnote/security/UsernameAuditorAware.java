package com.pliesveld.flashnote.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Gets the current user name of the currently authenticated user if applicable.  By implementing
 * AuditorAware we are exposing auditable entities to the Spring-Data repositories.
 */
@Service("auditorAware")
public class UsernameAuditorAware implements AuditorAware<String> {
    private static final Logger LOG = LogManager.getLogger();

    @Override
    public String getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated())
        {
            return null;
        }

        LOG.debug("getCurrentAuditor");
        if(authentication.getPrincipal() instanceof StudentPrincipal)
            return ((StudentPrincipal) authentication.getPrincipal()).getUsername();
        else {
            LOG.debug("auth.principal is actually: {} {}", authentication.getPrincipal().getClass().getName(),authentication.getPrincipal());
            return "SYSTEM";
        }
    }
}
