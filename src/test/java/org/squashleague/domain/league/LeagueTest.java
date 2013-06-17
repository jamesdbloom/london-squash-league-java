package org.squashleague.domain.league;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class LeagueTest {

    @Test
    public void shouldSort() {
        Club clubOne = new Club().withName("c_a");
        Club clubTwo = new Club().withName("c_b");
        Club clubThree = new Club().withName("c_c");
        League leagueOne = new League().withName("l_a").withClub(clubOne);
        League leagueTwo = new League().withName("l_b").withClub(clubOne);
        League leagueThree = new League().withName("l_d").withClub(clubTwo);
        League leagueFour = new League().withName("l_c").withClub(clubThree);

        List<League> leagues = Arrays.asList(leagueThree, leagueFour, leagueOne, leagueTwo);
        Collections.sort(leagues);

        assertArrayEquals(new League[]{leagueOne, leagueTwo, leagueThree, leagueFour}, leagues.toArray());
    }

    @Test
    public void shouldMerge() {
        League existing = new League()
                .withName("name")
                .withRounds(new Round(), new Round());

        League newVersion = new League()
                .withName("new name")
                .withRounds(new Round(), new Round(), new Round());

        assertEquals(newVersion, existing.merge(newVersion));
        assertEquals(existing, existing.merge(new League()));
    }
}
