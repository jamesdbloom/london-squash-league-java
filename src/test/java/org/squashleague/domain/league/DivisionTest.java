package org.squashleague.domain.league;

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
        Division divisionOne = new Division().withName("d_a").withLeague(leagueOne);
        Division divisionTwo = new Division().withName("d_b").withLeague(leagueOne);
        Division divisionThree = new Division().withName("d_d").withLeague(leagueTwo);
        Division divisionFour = new Division().withName("d_e").withLeague(leagueThree);
        Division divisionFive = new Division().withName("d_a").withLeague(leagueFour);

        List<Division> divisions = Arrays.asList(divisionFive, divisionThree, divisionFour, divisionOne, divisionTwo);
        Collections.sort(divisions);

        assertArrayEquals(new Division[]{divisionOne, divisionTwo, divisionThree, divisionFour, divisionFive}, divisions.toArray());
    }

    @Test
    public void shouldMerge() {
        Division existing = new Division()
                .withName("name")
                .withRounds(new Round(), new Round());

        Division newVersion = new Division()
                .withName("new name")
                .withRounds(new Round(), new Round(), new Round());

        assertEquals(newVersion, existing.merge(newVersion));
        assertEquals(existing, existing.merge(new Division()));
    }
}
