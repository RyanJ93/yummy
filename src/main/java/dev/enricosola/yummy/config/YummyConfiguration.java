package dev.enricosola.yummy.config;

import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import dev.enricosola.yummy.DTO.AppConfig;

@Configuration
@PropertySource("classpath:application.properties")
public class YummyConfiguration {
    private final Environment environment;

    public YummyConfiguration(Environment environment){
        this.environment = environment;
    }

    @Bean
    public AppConfig appConfig(){
        String adminUsername = this.environment.getProperty("yummy.admin_username");
        String adminPassword = this.environment.getProperty("yummy.admin_password");
        String storage = this.environment.getProperty("yummy.storage");
        return new AppConfig(storage, adminUsername, adminPassword);
    }
}
