package org.squashleague.domain.league;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MatchTest {

    @Test
    public void shouldMerge() {
        Match existing = new Match()
                .withPlayerOne(new Player().withStatus(PlayerStatus.ACTIVE))
                .withPlayerTwo(new Player().withStatus(PlayerStatus.ACTIVE))
                .withRound(new Round().withStartDate(new DateTime().plusDays(1)))
                .withScore("1-2");

        Match newVersion = new Match()
                .withPlayerOne(new Player().withStatus(PlayerStatus.INACTIVE))
                .withPlayerTwo(new Player().withStatus(PlayerStatus.INACTIVE))
                .withRound(new Round().withStartDate(new DateTime().plusDays(10)))
                .withScore("3-4");

        assertEquals(newVersion, existing.merge(newVersion));
        assertEquals(existing, existing.merge(new Match()));
    }
}
