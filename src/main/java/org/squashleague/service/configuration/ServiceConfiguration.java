package org.squashleague.service.configuration;

import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * @author jamesdbloom
 */
@Configuration
@ComponentScan(
        basePackages = {"org.squashleague.service"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class)
        }
)
@PropertySource({"classpath:application.properties"})
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
                setProperty("mail.smtp.starttls.enable", environment.getProperty("email.starttls"));
                setProperty("mail.smtp.quitwait", "false");
            }});
        }};
    }
}
