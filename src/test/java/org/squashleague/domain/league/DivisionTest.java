package org.squashleague.domain.league;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DivisionTest {

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
