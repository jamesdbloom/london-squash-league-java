package org.squashleague.domain.account;

import org.junit.Test;
import org.squashleague.domain.league.Club;

import static org.junit.Assert.assertEquals;

public class RoleTest {

    @Test
    public void shouldMerge() {
        Role existing = new Role()
                .withName("name")
                .withDescription("description")
                .withClub(new Club().withName("name"));

        Role newVersion = new Role()
                .withName("new name")
                .withDescription("new description")
                .withClub(new Club().withName("new name"));

        assertEquals(newVersion, existing.merge(newVersion));
        assertEquals(existing, existing.merge(new Role()));
    }
}
