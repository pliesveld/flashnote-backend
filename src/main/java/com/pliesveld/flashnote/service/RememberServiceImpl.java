package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.AccountRememberMeToken;
import com.pliesveld.flashnote.exception.ResourceNotFoundException;
import com.pliesveld.flashnote.repository.RememberTokenRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service(value = "rememberService")
@Transactional
public class RememberServiceImpl implements RememberService {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    RememberTokenRepository rememberTokenRepository;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        AccountRememberMeToken meToken = new AccountRememberMeToken(token);
        meToken = rememberTokenRepository.save(meToken);
        LOG.debug("Create RememberToken: {}", meToken.toString());
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        LOG.debug("Updating series: {} to token: {}", series, tokenValue);

        AccountRememberMeToken meToken = rememberTokenRepository.findOne(series);
        if(meToken != null)
        {
            meToken.setLastUsed(lastUsed.toInstant());
            meToken.setToken(tokenValue);
        } else {
            throw new ResourceNotFoundException(series);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        AccountRememberMeToken token = rememberTokenRepository.findOne(seriesId);

        if(token == null)
        {
            LOG.debug("No token found: {}", seriesId);
            throw new ResourceNotFoundException(seriesId);
        }

        LOG.debug("Get token: {}", token);
        return token.toPersistentRememberMeToken();
    }

    @Override
    public void removeUserTokens(String username) {
        LOG.debug("Removing token for: {}", username);
        rememberTokenRepository.deleteTokensForUser(username);
    }


}
