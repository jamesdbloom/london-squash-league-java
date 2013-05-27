package org.squashleague.domain.league;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LeagueTest {

    @Test
    public void shouldMerge() {
        League existing = new League()
                .withName("name")
                .withDivisions(new Division(), new Division());

        League newVersion = new League()
                .withName("new name")
                .withDivisions(new Division(), new Division(), new Division());

        assertEquals(newVersion, existing.merge(newVersion));
        assertEquals(existing, existing.merge(new League()));
    }
}
