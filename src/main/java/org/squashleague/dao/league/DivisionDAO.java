package org.squashleague.dao.league;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.squashleague.domain.league.Division;

/**
 * @author squashleague
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DivisionDAO extends AbstractJpaDAO<Division> {

    public DivisionDAO() {
        super(Division.class);
    }

}
