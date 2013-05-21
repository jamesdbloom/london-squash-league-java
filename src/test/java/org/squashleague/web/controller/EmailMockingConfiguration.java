package org.squashleague.web.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.squashleague.service.email.EmailService;
import org.squashleague.service.security.SpringSecurityUserContext;

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
