package org.squashleague.web.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.squashleague.service.security.SpringSecurityUserContext;

import static org.mockito.Mockito.mock;

/**
 * @author jamesdbloom
 */
@Configuration
public class SecurityMockingConfiguration {

    @Bean
    public SpringSecurityUserContext userContext() {
        return mock(SpringSecurityUserContext.class);
    }
}
