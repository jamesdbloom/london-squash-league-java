package org.squashleague.dao.league;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.squashleague.domain.league.Player;

/**
 * @author squashleague
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PlayerDAO extends AbstractJpaDAO<Player> {

    public PlayerDAO() {
        super(Player.class);
    }

}
