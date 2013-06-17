package org.squashleague.domain.league;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
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
        round.withEndDate(DATE_TIME.plusSeconds(1));

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
    public void shouldSort() {
        Club clubOne = new Club().withName("c_a");
        League leagueOne = new League().withName("l_a").withClub(clubOne);
        League leagueTwo = new League().withName("l_b").withClub(clubOne);
        Round roundOne = new Round().withStartDate(DATE_TIME.plusDays(1)).withEndDate(DATE_TIME).withLeague(leagueOne);
        Round roundTwo = new Round().withStartDate(DATE_TIME.plusDays(2)).withEndDate(DATE_TIME).withLeague(leagueOne);
        Round roundThree = new Round().withStartDate(DATE_TIME.plusDays(2)).withEndDate(DATE_TIME.plusDays(1)).withLeague(leagueOne);
        Round roundFour = new Round().withStartDate(DATE_TIME.plusDays(4)).withEndDate(DATE_TIME.plusDays(4)).withLeague(leagueOne);
        Round roundFive = new Round().withStartDate(DATE_TIME).withEndDate(DATE_TIME).withLeague(leagueTwo);


        List<Round> rounds = Arrays.asList(roundFive, roundTwo, roundThree, roundFour, roundOne);
        Collections.sort(rounds);

        assertArrayEquals(new Round[]{roundOne, roundTwo, roundThree, roundFour, roundFive}, rounds.toArray());
    }

    @Test
    public void shouldMerge() {
        Round existing = new Round()
                .withStartDate(DATE_TIME.plusDays(1))
                .withEndDate(DATE_TIME.plusDays(2))
                .withLeague(new League().withName("old"));

        Round newVersion = new Round()
                .withStartDate(DATE_TIME.plusDays(10))
                .withEndDate(DATE_TIME.plusDays(20))
                .withLeague(new League().withName("new"));

        assertEquals(newVersion, existing.merge(newVersion));
        assertEquals(existing, existing.merge(new Round()));
    }
}
