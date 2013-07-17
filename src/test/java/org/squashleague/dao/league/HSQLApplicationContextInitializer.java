package org.squashleague.dao.league;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.mock.env.MockPropertySource;

public class HSQLApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();
        MockPropertySource mockEnvVars = new MockPropertySource() {{
            withProperty("jdbc.driverClassName", false);
            withProperty("jdbc.driverClassName", "org.hsqldb.jdbcDriver");
            withProperty("jdbc.url", "jdbc:hsqldb:mem:db143442_squash");
            withProperty("jdbc.username", "sa");
            withProperty("jdbc.password", "");
            withProperty("jdbc.validationQuery", "");
            withProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
            withProperty("hibernate.show_sql", "false");
            withProperty("hibernate.hbm2ddl.auto", "create-drop");
            withProperty("jpa.generateDdl", "true");
        }};
        propertySources.replace(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, mockEnvVars);
    }
}