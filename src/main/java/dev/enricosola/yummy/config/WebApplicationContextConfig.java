package dev.enricosola.yummy.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.http.CacheControl;
import dev.enricosola.yummy.DTO.AppConfig;
import java.time.Duration;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "dev.enricosola.yummy")
public class WebApplicationContextConfig implements WebMvcConfigurer {
    private final AppConfig appConfig;

    public WebApplicationContextConfig(AppConfig appConfig){
        this.appConfig = appConfig;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry) {
        ResourceHandlerRegistration storageRegistration = resourceHandlerRegistry.addResourceHandler("/storage/**");
        ResourceHandlerRegistration assetRegistration = resourceHandlerRegistry.addResourceHandler("/resources/**");
        storageRegistration.addResourceLocations("file://" + this.appConfig.getStorage() + "/");
        storageRegistration.setCacheControl(CacheControl.maxAge(Duration.ofDays(365)));
        assetRegistration.setCacheControl(CacheControl.maxAge(Duration.ofDays(365)));
        assetRegistration.addResourceLocations("classpath:/static/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer){
        configurer.enable();
    }
}
