package org.squashleague.domain.league;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RoundTest {

    public static final DateTime DATE_TIME = new DateTime();

    @Test
    public void shouldHaveFinishedStatus() {
        Round round = new Round();

        round.withStartDate(DATE_TIME.minusDays(10));
        round.withEndDate(DATE_TIME.minusDays(1));

        assertEquals(RoundStatus.FINISHED, round.getStatus());
    }

    @Test
    public void shouldHaveInPlayStatus() {
        Round round = new Round();

        round.withStartDate(DATE_TIME.minusDays(1));
        round.withEndDate(DATE_TIME.plusDays(1));

        assertEquals(RoundStatus.INPLAY, round.getStatus());
    }

    @Test
    public void shouldHaveInPlayStatusIfStartDateOnBorder() {
        Round round = new Round();

        round.withStartDate(DATE_TIME);
        round.withEndDate(DATE_TIME.plusDays(1));

        assertEquals(RoundStatus.INPLAY, round.getStatus());
    }

    @Test
    public void shouldHaveInPlayStatusIfEndDateOnBorder() {
        Round round = new Round();

        round.withStartDate(DATE_TIME.minusDays(1));
        round.withEndDate(DATE_TIME.plusMillis(100));

        assertEquals(RoundStatus.INPLAY, round.getStatus());
    }

    @Test
    public void shouldHaveStartingSoonStatus() {
        Round round = new Round();

        round.withStartDate(DATE_TIME.plusDays(1));
        round.withEndDate(DATE_TIME.plusDays(10));

        assertEquals(RoundStatus.STARTING_SOON, round.getStatus());
    }

    @Test
    public void shouldHaveStartingSoonStatusIfStartDateOnBorder() {
        Round round = new Round();

        round.withStartDate(DATE_TIME.plusDays(2));
        round.withEndDate(DATE_TIME.plusDays(2));

        assertEquals(RoundStatus.STARTING_SOON, round.getStatus());
    }

    @Test
    public void shouldHaveNotStartedStatus() {
        Round round = new Round();

        round.withStartDate(DATE_TIME.plusDays(5));
        round.withEndDate(DATE_TIME.plusDays(10));

        assertEquals(RoundStatus.NOT_STARTED, round.getStatus());
    }

    @Test
    public void shouldMerge() {
        Round existing = new Round()
                .withStartDate(DATE_TIME.plusDays(1))
                .withEndDate(DATE_TIME.plusDays(2))
                .withDivision(new Division().withName("old"));

        Round newVersion = new Round()
                .withStartDate(DATE_TIME.plusDays(10))
                .withEndDate(DATE_TIME.plusDays(20))
                .withDivision(new Division().withName("new"));

        assertEquals(newVersion, existing.merge(newVersion));
        assertEquals(existing, existing.merge(new Round()));
    }
}
