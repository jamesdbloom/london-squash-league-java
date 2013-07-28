package org.squashleague.dao.league;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.squashleague.dao.AbstractJpaDAO;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.League;
import org.squashleague.domain.league.Player;
import org.squashleague.domain.league.PlayerStatus;

import java.util.List;

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
    public List<Player> findAllActiveByUser(User user) {
        return entityManager.createQuery("from Player as player where player.status = " + PlayerStatus.ACTIVE.ordinal() + " and player.user.id = " + user.getId(), Player.class).getResultList();
    }

    @Transactional
    public List<Player> findAllByUser(User user) {
        return entityManager.createQuery("from Player as player where player.user.id = " + user.getId(), Player.class).getResultList();
    }

    public List<Player> findAllActiveByLeague(League league) {
        return entityManager.createQuery("from Player as player where player.status = " + PlayerStatus.ACTIVE.ordinal() + " and player.league.id = " + league.getId() + " order by player.user.name", Player.class).getResultList();
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #player.user.id")
    public void save(Player player) {
        entityManager.persist(player);
        entityManager.flush();
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #player.user.id")
    public void updateStatus(Player player, PlayerStatus status) {
        entityManager.createQuery("update Player as player set player.status = " + status.ordinal() + " where player.id = " + player.getId()).executeUpdate();
    }
}
