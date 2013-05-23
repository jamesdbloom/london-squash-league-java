package org.squashleague.service.email;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

/**
 * @author jamesdbloom
 */
@Configuration
public class EmailMockingConfiguration {

    @Bean
    public EmailService emailService() {
        return mock(EmailService.class);
    }
}
