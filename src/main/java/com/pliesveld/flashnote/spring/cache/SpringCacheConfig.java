package com.pliesveld.flashnote.spring.cache;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@ComponentScan(basePackages = "com.pliesveld.flashnote.security")
public class SpringCacheConfig {
    @Bean(name = "localGauvaCaches")
    public SimpleCacheManager localGuavaCaches() {
        final SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        final GuavaCache tokenCache = new GuavaCache(CacheConstants.TOKEN_CACHE, CacheBuilder.newBuilder()
                .concurrencyLevel(3) //Choose per your own will.
                .expireAfterAccess(2, TimeUnit.MINUTES) //Expire if not accessed within 2 minutes.
                .maximumSize(1000).build()); //For a maximum of 1000 User Objects.

        simpleCacheManager.setCaches(Arrays.asList(tokenCache));

        return simpleCacheManager;
    }

    public static class CacheConstants {
        public static final String TOKEN_CACHE = "TOKEN_CACHE";
    }
}
