package com.pliesveld.flashnote.security;

import com.pliesveld.flashnote.logging.Markers;
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
        if (authentication == null || !authentication.isAuthenticated())
        {
            LOG.trace(Markers.AUDIT, "No authentication object or is not authenticated.", authentication);
            return null;
        }

        LOG.trace(Markers.AUDIT, "Checking credentials for {}", authentication);

        Object principal = authentication.getPrincipal();
        Object details = authentication.getDetails();

        if (principal != null) {
            LOG.trace(Markers.AUDIT, "Principal {} : {}",principal.getClass().getName(), principal);
        }

        if (details != null) {
            LOG.trace(Markers.AUDIT, "Details {} : {}",details.getClass().getName(), details);
        }

//        Object credentials = authentication.getCredentials();
//        Collection<?> authorities = authentication.getAuthorities();

        if (principal instanceof StudentPrincipal)
        {
            StudentPrincipal studentPrincipal = (StudentPrincipal) principal;
            LOG.debug(Markers.AUDIT, "Found StudentPrincipal : {} ", studentPrincipal);
            return studentPrincipal.getUsername();
        }
        else {
            LOG.debug(Markers.AUDIT, "Principal {} : {}",principal.getClass().getName(), principal);
            return "SYSTEM";
        }
    }
}
