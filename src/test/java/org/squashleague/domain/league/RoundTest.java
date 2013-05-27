package org.squashleague.domain.league;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RoundTest {

    @Test
    public void shouldHaveFinishedStatus() {
        Round round = new Round();

        round.setStartDate(new DateTime().minusDays(10));
        round.setEndDate(new DateTime().minusDays(1));

        assertEquals(RoundStatus.FINISHED, round.getStatus());
    }

    @Test
    public void shouldHaveInPlayStatus() {
        Round round = new Round();

        round.setStartDate(new DateTime().minusDays(1));
        round.setEndDate(new DateTime().plusDays(1));

        assertEquals(RoundStatus.INPLAY, round.getStatus());
    }

    @Test
    public void shouldHaveInPlayStatusIfStartDateOnBorder() {
        Round round = new Round();

        round.setStartDate(new DateTime());
        round.setEndDate(new DateTime().plusDays(1));

        assertEquals(RoundStatus.INPLAY, round.getStatus());
    }

    @Test
    public void shouldHaveInPlayStatusIfEndDateOnBorder() {
        Round round = new Round();

        round.setStartDate(new DateTime().minusDays(1));
        round.setEndDate(new DateTime());

        assertEquals(RoundStatus.INPLAY, round.getStatus());
    }

    @Test
    public void shouldHaveStartingSoonStatus() {
        Round round = new Round();

        round.setStartDate(new DateTime().plusDays(1));
        round.setEndDate(new DateTime().plusDays(10));

        assertEquals(RoundStatus.STARTING_SOON, round.getStatus());
    }

    @Test
    public void shouldHaveStartingSoonStatusIfStartDateOnBorder() {
        Round round = new Round();

        round.setStartDate(new DateTime().plusDays(2));
        round.setEndDate(new DateTime().plusDays(2));

        assertEquals(RoundStatus.STARTING_SOON, round.getStatus());
    }

    @Test
    public void shouldHaveNotStartedStatus() {
        Round round = new Round();

        round.setStartDate(new DateTime().plusDays(5));
        round.setEndDate(new DateTime().plusDays(10));

        assertEquals(RoundStatus.NOT_STARTED, round.getStatus());
    }
}
