package org.squashleague.dao.league;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.squashleague.dao.AbstractJpaDAO;
import org.squashleague.domain.league.*;

import java.util.List;

/**
 * @author jamesdbloom
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DivisionDAO extends AbstractJpaDAO<Division> {

    public DivisionDAO() {
        super(Division.class);
    }

    public List<Division> findAllByRound(Round round) {
        return entityManager.createQuery("from Division as division where division.round.id = " + round.getId(), Division.class).getResultList();
    }
}
