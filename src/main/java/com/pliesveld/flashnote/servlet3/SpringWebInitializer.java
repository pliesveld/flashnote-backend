package com.pliesveld.flashnote.servlet3;

/**
 * Created by happs on 1/18/16.
 */
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import java.io.File;

import com.pliesveld.flashnote.spring.SpringRootConfig;
import com.pliesveld.flashnote.spring.SpringWebConfig;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class SpringWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer implements WebApplicationInitializer {

    private int maxUploadSizeInMb = 5 * 1024 * 1024; // 5 MB


    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { SpringRootConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { SpringWebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
    /*
//TODO: http://joshlong.com/jl/blogPost/simplified_web_configuration_with_spring.html
     *
     *     @Override
     *         protected Filter[] getServletFilters() {
     *                 return new Filter[]{new HiddenHttpMethodFilter(), new MultipartFilter(), new OpenEntityManagerInViewFilter()};
     *                     }

// notifies session creation / destruction
    @Override
    protected void registerDispatcherServlet(ServletContext servletContext) {
        super.registerDispatcherServlet(servletContext);

        servletContext.addListener(new HttpSessionEventPublisher());

    }



     *
     */

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        // additional configuration, here for MultipartConfig
        super.customizeRegistration(registration);
        String uploadDirectory = ""; //ServiceConfiguration.CRM_STORAGE_UPLOADS_DIRECTORY;

        MultipartConfigElement multipartConf = new MultipartConfigElement(uploadDirectory, maxUploadSizeInMb, maxUploadSizeInMb*2, maxUploadSizeInMb/2);
        registration.setMultipartConfig(multipartConf);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        //servletContext.setInitParameter("spring.profiles.active", "hsql");

/*
        // Create the root Spring application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(SpringRootConfig.class);

        // Manage the lifecycle of the root application context
        servletContext.addListener(new ContextLoadListener(rootContext));

        // Create the dispatcher servlet's Spring application context
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(SpringWebConfig.class);

        // Register and map the dispatcher servlet
        ServletRegistration.Dynamic registration = servletContext.addServlet("dispatcher",dispatcherContext);
*/

        ServletRegistration.Dynamic registration = servletContext.addServlet("dispatcher",new DispatcherServlet());
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
    }

}
