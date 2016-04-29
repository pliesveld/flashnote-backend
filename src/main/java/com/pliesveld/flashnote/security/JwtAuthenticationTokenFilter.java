package com.pliesveld.flashnote.security;

import com.pliesveld.flashnote.logging.Markers;
import com.pliesveld.flashnote.spring.cache.SpringCacheConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/*
The MIT License (MIT)

Copyright (c) 2016 Stephan Zerhusen

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

https://github.com/szerhusenBC/jwt-spring-security-demo

With modifications to include the caching of jwt tokens
 */

public class JwtAuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenCache jwtTokenCache;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authToken = httpRequest.getHeader(this.tokenHeader);

        if (authToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            /*
                Check the token cache to see if the token was previously used to authenticate a user.
             */
            UserDetails userDetails = jwtTokenCache.findUserByTokenCache(authToken);

            if(userDetails == null)
            {
                /*
                    Verify that the token has not expired, and that the creation date is not before the
                    date last password reset of the user.  If successful cache the token with a timeout of
                    2 minutes.
                 */
                String username = jwtTokenUtil.getUsernameFromToken(authToken);
                userDetails = this.userDetailsService.loadUserByUsername(username);
                if (!jwtTokenUtil.validateToken(authToken, userDetails))
                    userDetails = null;
                else
                {
                    LOG.debug(Markers.SECURITY_AUTH_TOKEN, "Authenticating username {} with jwt {}", username, authToken);
                    jwtTokenCache.cacheUserByToken(authToken,userDetails);
                }
            }

            /*
                User has been successfully authenticated by the jwt token.  Assign an authentication object to the
                Spring Security context.
             */
            if(userDetails != null)
             {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }


    @Component
    static public class JwtTokenCache {

        @Cacheable(cacheNames = SpringCacheConfig.CacheConstants.TOKEN_CACHE, key = "#token", unless = "#result == null")
        public UserDetails findUserByTokenCache(String token) {
            return null;
        }

        @CachePut(cacheNames = SpringCacheConfig.CacheConstants.TOKEN_CACHE, key = "#token")
        public UserDetails cacheUserByToken(String token, UserDetails user)
        {
            return user;
        }
    }
}