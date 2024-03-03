package dev.enricosola.yummy.config;

import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import static org.hibernate.cfg.C3p0Settings.*;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan({ "dev.enricosola.yummy.config" })
@PropertySource("classpath:application.properties")
public class HibernateConfig {
    private final Environment environment;
    private final DataSource dataSource;

    private Properties hibernateProperties(){
        Properties jpaProperties = new Properties();
        jpaProperties.put("javax.persistence.schema-generation.database.action", "none");
        jpaProperties.put("hibernate.format_sql", this.environment.getRequiredProperty("hibernate.format_sql"));
        jpaProperties.put("hibernate.show_sql", this.environment.getRequiredProperty("hibernate.show_sql"));
        jpaProperties.put("hibernate.dialect", this.environment.getRequiredProperty("hibernate.dialect"));
        jpaProperties.put(C3P0_ACQUIRE_INCREMENT, this.environment.getProperty("hibernate.c3p0.acquire_increment"));
        jpaProperties.put(C3P0_MAX_STATEMENTS, this.environment.getProperty("hibernate.c3p0.max_statements"));
        jpaProperties.put(C3P0_MIN_SIZE, this.environment.getProperty("hibernate.c3p0.min_size"));
        jpaProperties.put(C3P0_MAX_SIZE, this.environment.getProperty("hibernate.c3p0.max_size"));
        jpaProperties.put(C3P0_TIMEOUT, this.environment.getProperty("hibernate.c3p0.timeout"));
        jpaProperties.put("hibernate.enable_lazy_load_no_trans", true);
        return jpaProperties;
    }

    public HibernateConfig(Environment environment, DataSource dataSource){
        this.environment = environment;
        this.dataSource = dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
        factory.setPackagesToScan("dev.enricosola.yummy.entity");
        factory.setJpaVendorAdapter(this.jpaVendorAdapter());
        factory.setJpaProperties(this.hibernateProperties());
        factory.setValidationMode(ValidationMode.NONE);
        factory.setDataSource(this.dataSource);
        return factory;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter(){
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setDatabasePlatform(this.environment.getRequiredProperty("hibernate.dialect"));
        hibernateJpaVendorAdapter.setGenerateDdl(false);
        hibernateJpaVendorAdapter.setShowSql(true);
        return hibernateJpaVendorAdapter;
    }

    @Bean
    public JpaTransactionManager transactionManager(){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(this.entityManagerFactory().getObject());
        return transactionManager;
    }
}
