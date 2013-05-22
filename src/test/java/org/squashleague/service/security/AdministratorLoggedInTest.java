package org.squashleague.service.security;

import org.junit.Before;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;

import javax.annotation.Resource;

/**
 * @author jamesdbloom
 */
public abstract class AdministratorLoggedInTest {

    @Resource
    private SpringSecurityUserContext securityUserContext;

    @Before
    public void setupUser() {
        securityUserContext.setCurrentUser(new User().withRole(Role.ROLE_ADMIN));
    }
}
