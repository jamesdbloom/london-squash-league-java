package org.squashleague.domain.league;

import org.junit.Test;
import org.squashleague.domain.account.User;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class PlayerTest {

    @Test
    public void shouldMerge() {
        Player existing = new Player()
                .withCurrentDivision(new Division().withName(0).withRound(new Round().withLeague(new League().withName("league"))))
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(new User().withName("user"));

        Player newVersion = new Player()
                .withCurrentDivision(new Division().withName(1).withRound(new Round().withLeague(new League().withName("new league"))))
                .withStatus(PlayerStatus.INACTIVE)
                .withUser(new User().withName("new user"));

        assertEquals(newVersion, existing.merge(newVersion));
        assertEquals(existing, existing.merge(new Player()));
    }

    @Test
    public void shouldCalculateTotalScore() {
        Player player = (Player) new Player().withId(5l);
        player.withCurrentDivision(((Division) new Division().withName(1).withId(1l)).withRound((Round) new Round().withId(1l)))
                .withMatches(Arrays.asList(
                        ((Match) new Match()
                                .withId(1l))
                                .withDivision(player.getCurrentDivision())
                                .withPlayerOne((Player) new Player().withId(1l))
                                .withPlayerTwo(player)
                                .withScore("0-1"), // 3.1
                        ((Match) new Match()
                                .withId(2l))
                                .withDivision(player.getCurrentDivision())
                                .withPlayerOne(player)
                                .withPlayerTwo((Player) new Player().withId(1l))
                                .withScore("2-0"), // 3.2
                        ((Match) new Match()
                                .withId(3l))
                                .withDivision(((Division) new Division().withName(1).withId(2l)).withRound((Round) new Round().withId(1l)))
                                .withPlayerOne(player)
                                .withPlayerTwo((Player) new Player().withId(1l))
                                .withScore("3-2") // 0 - different division
                ));

        assertEquals(player.calculateScore(player.getCurrentDivision()), 6.3, 0.01);
    }
}
