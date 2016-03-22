package com.pliesveld.flashnote.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service("rateLimitService")
public class RateLimitServiceImpl implements RateLimitService {
    private static final Logger LOG = LogManager.getLogger();

    private final int MAX_ATTEMPT = 10;
    private LoadingCache<String, Integer> attemptsCache;

    public RateLimitServiceImpl() {
        super();
        attemptsCache = CacheBuilder.newBuilder()
                //.expireAfterAccess(Constants.ACCESS_LIMIT_LOGIN_DELAY, TimeUnit.SECONDS)
                .expireAfterAccess(3, TimeUnit.SECONDS)
                .removalListener((notification) -> LOG.debug("Removing cached key {}", notification.getKey()))
                .build(new CacheLoader<String, Integer>() {
            @Override
            public Integer load(final String key) {
                return 1;
            }
        });
    }

    @Override
    public boolean isBlocked(String key) {
        boolean ret = false;
        try {
            ret = attemptsCache.get(key) > 0;
        } catch (ExecutionException e) {
            ret = false;
        }

        LOG.debug("Rate limiting access check for {} has returned {}",key,ret);
        return ret;
    }

    @Override
    public void recordRemoteAccess(String key) {
        LOG.debug("Record invocation {}", key);

        int attempts = 0;
        try {
            attempts = attemptsCache.get(key);
        } catch(final ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key,attempts);
    }
}
