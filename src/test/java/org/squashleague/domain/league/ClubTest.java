package org.squashleague.domain.league;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClubTest {

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
