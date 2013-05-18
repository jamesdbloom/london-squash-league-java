package org.squashleague.dao.account;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.squashleague.dao.league.AbstractJpaDAO;
import org.squashleague.domain.account.User;

/**
 * @author squashleague
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserDAO extends AbstractJpaDAO<User> {

    public UserDAO() {
        super(User.class);
    }

}
