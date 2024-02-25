package dev.enricosola.yummy.config;

import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class MultipartConfig {
    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
