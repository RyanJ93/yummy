package dev.enricosola.yummy.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration;

public class DispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    private static final long MAX_REQUEST_SIZE = 33358112;
    private static final long MAX_FILE_SIZE = 10485760;

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration){
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
            "/tmp",
            DispatcherServletInitializer.MAX_FILE_SIZE,
            DispatcherServletInitializer.MAX_REQUEST_SIZE,
            0
        );
        registration.setMultipartConfig(multipartConfigElement);
    }

    @Override
    protected Class<?>[] getRootConfigClasses(){
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses(){
        return new Class[]{WebApplicationContextConfig.class};
    }

    @Override
    protected String[] getServletMappings(){
        return new String[]{"/"};
    }
}
