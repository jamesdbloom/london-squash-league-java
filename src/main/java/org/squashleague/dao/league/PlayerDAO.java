package org.squashleague.dao.league;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.squashleague.dao.AbstractJpaDAO;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.Player;
import org.squashleague.domain.league.PlayerStatus;

/**
 * @author jamesdbloom
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PlayerDAO extends AbstractJpaDAO<Player> {

    public PlayerDAO() {
        super(Player.class);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #player.user.id")
    public void updateStatus(Player player, PlayerStatus status) {
        entityManager.createQuery("update Player as player set player.status = " + status.ordinal() + " where player.id = " + player.getId()).executeUpdate();
    }
}
