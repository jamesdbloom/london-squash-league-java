package org.squashleague.dao.configuration;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;

/**
 * @author squashleague
 */
//@Configuration
@EnableTransactionManagement
@ComponentScan({"org.squashleague.dao"})
@PropertySource({"classpath:database.properties"})
public class PersistenceJPAConfig {

    @Resource
    private Environment environment;

    @Bean
    public BasicDataSource dataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(environment.getProperty("database.driverClassName"));
        basicDataSource.setUrl(environment.getProperty("database.url"));
        basicDataSource.setUsername(environment.getProperty("database.username"));
        basicDataSource.setPassword(environment.getProperty("database.password"));
        basicDataSource.setTestOnBorrow(true);
        basicDataSource.setTestOnReturn(true);
        basicDataSource.setTestWhileIdle(true);
        basicDataSource.setTimeBetweenEvictionRunsMillis(1800000);
        basicDataSource.setNumTestsPerEvictionRun(3);
        basicDataSource.setMinEvictableIdleTimeMillis(1800000);
        return basicDataSource;
//        return new DriverManagerDataSource() {{
//            setDriverClassName(environment.getProperty("jdbc.driverClassName"));
//            setUrl(environment.getProperty("jdbc.url"));
//            setUsername(environment.getProperty("jdbc.username"));
//            setPassword(environment.getProperty("jdbc.password"));
//        }};
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory());
        return jpaTransactionManager;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        return new LocalContainerEntityManagerFactoryBean() {{
            setPersistenceUnitName("persistenceUnit");
            setDataSource(dataSource());
        }}.getObject();
    }
}
