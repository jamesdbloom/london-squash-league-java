package org.squashleague.domain.league;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class DivisionTest {

    @Test
    public void shouldSort() {
        Club clubOne = new Club().withName("c_a");
        Club clubTwo = new Club().withName("c_b");
        Club clubThree = new Club().withName("c_c");
        League leagueOne = new League().withName("l_a").withClub(clubOne);
        League leagueTwo = new League().withName("l_b").withClub(clubOne);
        League leagueThree = new League().withName("l_d").withClub(clubTwo);
        League leagueFour = new League().withName("l_c").withClub(clubThree);
        Round roundOne = new Round().withStartDate(new DateTime().plusDays(1)).withEndDate(new DateTime().plusDays(2)).withLeague(leagueOne);
        Round roundTwo = new Round().withStartDate(new DateTime().plusDays(1)).withEndDate(new DateTime().plusDays(2)).withLeague(leagueTwo);
        Round roundThree = new Round().withStartDate(new DateTime().plusDays(1)).withEndDate(new DateTime().plusDays(2)).withLeague(leagueThree);
        Round roundFour = new Round().withStartDate(new DateTime().plusDays(1)).withEndDate(new DateTime().plusDays(2)).withLeague(leagueFour);
        Division divisionOne = new Division().withName(0).withRound(roundOne);
        Division divisionTwo = new Division().withName(1).withRound(roundOne);
        Division divisionThree = new Division().withName(4).withRound(roundTwo);
        Division divisionFour = new Division().withName(5).withRound(roundThree);
        Division divisionFive = new Division().withName(0).withRound(roundFour);

        List<Division> divisions = Arrays.asList(divisionFive, divisionThree, divisionFour, divisionOne, divisionTwo);
        Collections.sort(divisions);

        assertArrayEquals(new Division[]{divisionOne, divisionTwo, divisionThree, divisionFour, divisionFive}, divisions.toArray());
    }

    @Test
    public void shouldMerge() {
        Division existing = new Division()
                .withName(0)
                .withRound(new Round().withLeague(new League().withName("league")));

        Division newVersion = new Division()
                .withName(1)
                .withRound(new Round().withLeague(new League().withName("new league")));

        assertEquals(newVersion, existing.merge(newVersion));
        assertEquals(existing, existing.merge(new Division()));
    }
}
