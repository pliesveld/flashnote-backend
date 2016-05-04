package com.pliesveld.flashnote.security;

import com.pliesveld.flashnote.logging.Markers;
import com.pliesveld.flashnote.spring.cache.SpringCacheConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
* Created by patrick on 5/4/16.
*/
@Component
public class JwtTokenCache {
    private static final Logger LOG = LogManager.getLogger();

    @Cacheable(cacheNames = SpringCacheConfig.CacheConstants.TOKEN_CACHE, key = "#token", unless = "#result == null")
    public UserDetails findUserByTokenCache(String token) {
        LOG.debug(Markers.SECURITY_CACHE, "Checking cache for {}", () -> token);
        return null;
    }

    @CachePut(cacheNames = SpringCacheConfig.CacheConstants.TOKEN_CACHE, key = "#token")
    public UserDetails cacheUserByToken(String token, UserDetails user)
    {
        LOG.debug(Markers.SECURITY_CACHE, "Caching token {} to user {}", token, user);
        return user;
    }
}
