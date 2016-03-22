package com.pliesveld.flashnote.web.controller;

import com.pliesveld.flashnote.exception.TooManyRequestsException;
import com.pliesveld.flashnote.schema.Constants;
import com.pliesveld.flashnote.service.RateLimitService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RateLimitingInterceptor extends HandlerInterceptorAdapter {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private RateLimitService rateLimitService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOG.debug("RateLimitingInterceptor#preHandle");
        if (HandlerMethod.class.isAssignableFrom(handler.getClass())) {
            rateLimit(request, (HandlerMethod) handler);
        }
        return super.preHandle(request,response,handler);
    }

    private void rateLimit(HttpServletRequest request, HandlerMethod handlerMethod) {
        //if(handlerMethod.getMethodAnnotation(RateLimited.class) != null) {
        String ipAddr = request.getRemoteAddr();

        LOG.debug("Rate limiting request has been made from {}",ipAddr);

        if(rateLimitService.isBlocked(ipAddr)) {
            throw new TooManyRequestsException(Constants.ACCESS_LIMIT_LOGIN_DELAY);
        } else {
            rateLimitService.recordRemoteAccess(ipAddr);
        }
        //}
    }
}
