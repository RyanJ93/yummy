package dev.enricosola.yummy.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import java.time.Duration;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "dev.enricosola.yummy")
public class WebApplicationContextConfig implements WebMvcConfigurer {
    @Value("${yummy.storage}")
    private String storage;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry) {
        ResourceHandlerRegistration storageRegistration = resourceHandlerRegistry.addResourceHandler("/storage/**");
        ResourceHandlerRegistration assetRegistration = resourceHandlerRegistry.addResourceHandler("/resources/**");
        assetRegistration.setCacheControl(CacheControl.maxAge(Duration.ofDays(365)));
        assetRegistration.addResourceLocations("classpath:/static/");
        storageRegistration.setCacheControl(CacheControl.maxAge(Duration.ofDays(365)));
        storageRegistration.addResourceLocations("file://" + this.storage + "/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer){
        configurer.enable();
    }
}
