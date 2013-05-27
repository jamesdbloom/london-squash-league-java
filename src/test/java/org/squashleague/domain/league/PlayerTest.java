package org.squashleague.domain.league;

import org.junit.Test;
import org.squashleague.domain.account.User;

import static org.junit.Assert.assertEquals;

public class PlayerTest {

    @Test
    public void shouldMerge() {
        Player existing = new Player()
                .withCurrentDivision(new Division().withName("division"))
                .withStatus(PlayerStatus.ACTIVE)
                .withUser(new User().withName("user"));

        Player newVersion = new Player()
                .withCurrentDivision(new Division().withName("new division"))
                .withStatus(PlayerStatus.INACTIVE)
                .withUser(new User().withName("new user"));

        assertEquals(newVersion, existing.merge(newVersion));
        assertEquals(existing, existing.merge(new Player()));
    }
}
