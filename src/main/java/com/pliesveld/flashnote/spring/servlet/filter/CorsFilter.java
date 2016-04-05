package com.pliesveld.flashnote.spring.servlet.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse http_response = (HttpServletResponse) response;
        HttpServletRequest http_request = (HttpServletRequest) request;
        http_response.setHeader("Access-Control-Allow-Origin", "*");
        http_response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        http_response.setHeader("Access-Control-Max-Age", "3600");
        http_response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me, X-AUTH-TOKEN");

        if (http_request.getMethod() != "OPTIONS") {
            chain.doFilter(request, response);
        } else {
        }


    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }



    @Override
    public void destroy() {

    }
}
