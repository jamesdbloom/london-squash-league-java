package org.squashleague.service.configuration;

import freemarker.template.TemplateException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.format.FormatterRegistry;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.dao.league.*;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.*;
import org.squashleague.web.interceptor.bundling.AddBundlingModelToViewModelInterceptor;
import org.squashleague.web.interceptor.bundling.WroModelHolder;
import ro.isdc.wro.manager.factory.ConfigurableWroManagerFactory;
import ro.isdc.wro.manager.factory.WroManagerFactory;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Properties;

/**
 * @author jamesdbloom
 */
@Configuration
@ComponentScan(basePackages = {"org.squashleague.service"})
@PropertySource({"classpath:mail.properties"})
public class ServiceConfiguration {

    @Resource
    private Environment environment;

    @Bean
    public MailSender mailSender() {
        return new JavaMailSenderImpl() {{
            setHost(environment.getProperty("email.host"));
            setPort(environment.getProperty("email.port", Integer.class));
            setProtocol(environment.getProperty("email.protocol"));
            setUsername(environment.getProperty("email.username"));
            setPassword(environment.getProperty("email.password"));
            setJavaMailProperties(new Properties() {{ // https://javamail.java.net/nonav/docs/api/com/sun/mail/smtp/package-summary.html
                setProperty("mail.smtp.auth", "true");
                setProperty("mail.smtp.starttls.enable", "false");
                setProperty("mail.smtp.quitwait", "false");
            }});
        }};
    }
}
