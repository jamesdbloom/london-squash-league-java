package org.squashleague.service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

/**
 * @author jamesdbloom
 */
@Configuration
public class SecurityMockingConfiguration {

    @Bean
    public SpringSecurityAuthenticationProvider springSecurityAuthenticationProvider() {
        return mock(SpringSecurityAuthenticationProvider.class);
    }

    @Bean
    public SpringSecurityUserContext securityUserContext() {
        return mock(SpringSecurityUserContext.class);
    }
}
