package org.squashleague.domain.league;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ClubTest {

    @Test
    public void shouldSort() {
        Club clubOne = new Club().withName("a");
        Club clubTwo = new Club().withName("b");
        Club clubThree = new Club().withName("c");

        List<Club> clubs = Arrays.asList(clubOne, clubThree, clubTwo);
        Collections.sort(clubs);

        assertArrayEquals(new Club[]{clubOne, clubTwo, clubThree}, clubs.toArray());
    }

    @Test
    public void shouldMerge() {
        Club existing = new Club()
                .withName("name")
                .withAddress("address")
                .withLeagues(new League(), new League());

        Club newVersion = new Club()
                .withName("new name")
                .withAddress("new address")
                .withLeagues(new League(), new League(), new League());

        assertEquals(newVersion, existing.merge(newVersion));
        assertEquals(existing, existing.merge(new Club()));
    }
}
