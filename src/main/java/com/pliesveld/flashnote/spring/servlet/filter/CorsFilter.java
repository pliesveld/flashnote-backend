package com.pliesveld.flashnote.spring.servlet.filter;

import com.pliesveld.flashnote.logging.Markers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Component
public class CorsFilter implements Filter {
    private static final Logger LOG = LogManager.getLogger();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

//        HttpServletRequest http_request = (HttpServletRequest) request;

        HttpServletResponse http_response = (HttpServletResponse) response;
        http_response.setHeader("Access-Control-Allow-Origin", "*");
        http_response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        http_response.setHeader("Access-Control-Max-Age", "86400");
        http_response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me, X-AUTH-TOKEN");

        chain.doFilter(request, response);
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        StringBuilder sb = new StringBuilder(512);

        sb.append("Adding CORS filter ").append(filterConfig.getFilterName()).append('\n');

        Enumeration <String> params = filterConfig.getInitParameterNames();
        sb.append("With filter params: ").append('\n');
        while( params.hasMoreElements() )
        {
            final String param = params.nextElement();
            sb.append(param)
              .append(" = ")
              .append(filterConfig.getInitParameter(param)).append('\n');
        }

        LOG.debug(Markers.SERVLET_INIT,sb.toString());

    }



    @Override
    public void destroy() {

    }
}
