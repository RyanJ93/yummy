package dev.enricosola.yummy.config;

import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import dev.enricosola.yummy.utils.ConfigUtils;
import dev.enricosola.yummy.DTO.AppConfig;

@Configuration
@PropertySource("classpath:application.properties")
public class YummyConfiguration {
    private final Environment environment;
    private final ConfigUtils configUtils;

    public YummyConfiguration(Environment environment, ConfigUtils configUtils){
        this.environment = environment;
        this.configUtils = configUtils;
    }

    @Bean
    public AppConfig appConfig(){
        String adminUsername = this.configUtils.getRequiredProperty("ADMIN_USERNAME", "yummy.admin_username");
        String adminPassword = this.configUtils.getRequiredProperty("ADMIN_PASSWORD", "yummy.admin_password");
        String storage = this.environment.getRequiredProperty("yummy.storage");
        return new AppConfig(storage, adminUsername, adminPassword);
    }
}
