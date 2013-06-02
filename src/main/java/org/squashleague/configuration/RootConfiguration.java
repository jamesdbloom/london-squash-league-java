package org.squashleague.configuration;

import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.squashleague.service.configuration.ServiceConfiguration;
import ro.isdc.wro.manager.factory.ConfigurableWroManagerFactory;
import ro.isdc.wro.manager.factory.WroManagerFactory;

import javax.annotation.Resource;
import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * This configuration contains top level beans and any configuration required by filters (as WebMvcConfiguration only loaded within Dispatcher Servlet)
 *
 * @author jamesdbloom
 */
@Configuration
@PropertySource({"classpath:application.properties", "classpath:ValidationMessages.properties"})
@ImportResource(value = {"classpath:/config/security-context.xml", "classpath:/config/dao-context.xml"})
@Import(ServiceConfiguration.class)
public class RootConfiguration {
    @Resource
    private Environment environment;

    @Bean
    public WroManagerFactory wroManagerFactory() {
        ConfigurableWroManagerFactory wroManagerFactory = new ConfigurableWroManagerFactory();

        wroManagerFactory.setConfigProperties(new Properties() {{
            setProperty("debug", environment.getProperty("bundling.enabled"));
            setProperty("preProcessors", "cssImport,semicolonAppender,conformColors");
            setProperty("postProcessors", "yuiCssMin,googleClosureAdvanced");
            setProperty("cacheGzippedContent", "true");
            setProperty("hashStrategy", "MD5"); // should drive the naming strategy to fingerprint resource urls - NOT YET WORKING / CONFIGURED CORRECTLY
            setProperty("namingStrategy", "hashEncoder-CRC32"); // should drive the naming strategy to fingerprint resource urls - NOT YET WORKING / CONFIGURED CORRECTLY
        }});

        return wroManagerFactory;
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        return new ThreadPoolTaskExecutor() {{
            setCorePoolSize(5);
            setMaxPoolSize(10);
            setQueueCapacity(50);
            setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        }};
    }
}
