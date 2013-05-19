package org.squashleague.dao.account;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.squashleague.dao.league.AbstractJpaDAO;
import org.squashleague.domain.account.Role;

import javax.annotation.PostConstruct;
import javax.persistence.EntityTransaction;

/**
 * @author jamesdbloom
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RoleDAO extends AbstractJpaDAO<Role> {

//    @Value("${hibernate.hbm2ddl.auto}")
//    private String hbm2ddl;
//    @Value("${hibernate.dialect}")
//    private String dialect;

    public RoleDAO() {
        super(Role.class);
    }

//    @PostConstruct
//    @Transactional
//    public void initializeRoles() {
//        if (hbm2ddl.equals("create-drop") || dialect.equals("org.hibernate.dialect.HSQLDialect")) {
//            save(Role.ROLE_ANONYMOUS);
//            save(Role.ROLE_USER);
//            save(Role.ROLE_ADMIN);
//        }
//    }

}
