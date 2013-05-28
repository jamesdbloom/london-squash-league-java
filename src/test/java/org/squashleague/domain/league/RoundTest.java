package org.squashleague.domain.league;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RoundTest {

    @Test
    public void shouldHaveFinishedStatus() {
        Round round = new Round();

        round.withStartDate(new DateTime().minusDays(10));
        round.withEndDate(new DateTime().minusDays(1));

        assertEquals(RoundStatus.FINISHED, round.getStatus());
    }

    @Test
    public void shouldHaveInPlayStatus() {
        Round round = new Round();

        round.withStartDate(new DateTime().minusDays(1));
        round.withEndDate(new DateTime().plusDays(1));

        assertEquals(RoundStatus.INPLAY, round.getStatus());
    }

    @Test
    public void shouldHaveInPlayStatusIfStartDateOnBorder() {
        Round round = new Round();

        round.withStartDate(new DateTime());
        round.withEndDate(new DateTime().plusDays(1));

        assertEquals(RoundStatus.INPLAY, round.getStatus());
    }

    @Test
    public void shouldHaveInPlayStatusIfEndDateOnBorder() {
        Round round = new Round();

        round.withStartDate(new DateTime().minusDays(1));
        round.withEndDate(new DateTime());

        assertEquals(RoundStatus.INPLAY, round.getStatus());
    }

    @Test
    public void shouldHaveStartingSoonStatus() {
        Round round = new Round();

        round.withStartDate(new DateTime().plusDays(1));
        round.withEndDate(new DateTime().plusDays(10));

        assertEquals(RoundStatus.STARTING_SOON, round.getStatus());
    }

    @Test
    public void shouldHaveStartingSoonStatusIfStartDateOnBorder() {
        Round round = new Round();

        round.withStartDate(new DateTime().plusDays(2));
        round.withEndDate(new DateTime().plusDays(2));

        assertEquals(RoundStatus.STARTING_SOON, round.getStatus());
    }

    @Test
    public void shouldHaveNotStartedStatus() {
        Round round = new Round();

        round.withStartDate(new DateTime().plusDays(5));
        round.withEndDate(new DateTime().plusDays(10));

        assertEquals(RoundStatus.NOT_STARTED, round.getStatus());
    }

    @Test
    public void shouldMerge() {
        Round existing = new Round()
                .withStartDate(new DateTime().plusDays(1))
                .withEndDate(new DateTime().plusDays(2))
                .withDivision(new Division().withName("old"));

        Round newVersion = new Round()
                .withStartDate(new DateTime().plusDays(10))
                .withEndDate(new DateTime().plusDays(20))
                .withDivision(new Division().withName("new"));

        assertEquals(newVersion, existing.merge(newVersion));
        assertEquals(existing, existing.merge(new Round()));
    }
}
