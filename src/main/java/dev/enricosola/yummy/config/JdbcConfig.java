package dev.enricosola.yummy.config;

import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.core.env.Environment;
import dev.enricosola.yummy.utils.ConfigUtils;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "dev.enricosola.yummy.config")
@PropertySource(value = { "classpath:application.properties" })
public class JdbcConfig {
    private final Environment environment;
    private final ConfigUtils configUtils;

    public JdbcConfig(Environment environment, ConfigUtils configUtils){
        this.environment = environment;
        this.configUtils = configUtils;
    }

    @Bean(name = "dataSource")
    public DataSource dataSource(){
        String username = this.configUtils.getRequiredProperty("SPRING_DATASOURCE_USERNAME", "jdbc.username");
        String password = this.configUtils.getRequiredProperty("SPRING_DATASOURCE_PASSWORD", "jdbc.password");
        String url = this.configUtils.getRequiredProperty("SPRING_DATASOURCE_URL", "jdbc.url");
        String driverClassName = this.environment.getRequiredProperty("jdbc.driverClassName");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setUrl(url);
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate getJdbcTemplate(DataSource dataSource){
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
