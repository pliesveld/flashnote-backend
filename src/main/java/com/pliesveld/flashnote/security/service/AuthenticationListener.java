package com.pliesveld.flashnote.security.service;


import com.pliesveld.flashnote.logging.Markers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationListener {
    private static final Logger LOG = LogManager.getLogger();

    @EventListener
    public void authSuccess(AuthenticationSuccessEvent authEvent) {
        LOG.debug(Markers.SECURITY_AUTH, "Authentication Success {} : {}", authEvent.getTimestamp(), authEvent.getAuthentication());
    }

    @EventListener
    public void authFailure(AuthenticationFailureBadCredentialsEvent authEvent) {
        LOG.debug(Markers.SECURITY_AUTH, "Authentication Failure {} : {}", authEvent.getTimestamp(), authEvent.getAuthentication());
    }
}
