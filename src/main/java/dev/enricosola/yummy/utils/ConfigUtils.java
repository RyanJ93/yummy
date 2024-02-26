package dev.enricosola.yummy.utils;

import dev.enricosola.yummy.exception.MisconfigurationException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ConfigUtils {
    private final Environment environment;

    public ConfigUtils(Environment environment){
        this.environment = environment;
    }

    public String getProperty(String envVar, String propertyName){
        String propertyValue = System.getenv(envVar);
        if ( propertyValue == null ){
            propertyValue = this.environment.getProperty(propertyName);
        }
        if ( propertyValue != null && propertyValue.isBlank() ){
            propertyValue = null;
        }
        return propertyValue;
    }

    public String getRequiredProperty(String envVar, String propertyName){
        String propertyValue = this.getProperty(envVar, propertyName);
        if ( propertyValue == null ){
            throw new MisconfigurationException("No value defined for property " + propertyName);
        }
        return propertyValue;
    }
}
