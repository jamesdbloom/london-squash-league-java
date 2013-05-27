package org.squashleague.domain.account;

import org.junit.Test;
import org.squashleague.domain.league.Player;

import static org.junit.Assert.assertEquals;

public class UserTest {

    @Test
    public void shouldMerge() {
        User existing = new User()
                .withName("name")
                .withEmail("email")
                .withMobile("mobile")
                .withMobilePrivacy(MobilePrivacy.SECRET)
                .withOneTimeToken("oneTimeToken")
                .withPassword("password")
                .withRole(Role.ROLE_ANONYMOUS)
                .withPlayers(new Player(), new Player());

        User newVersion = new User()
                .withName("new name")
                .withEmail("new email")
                .withMobile("new mobile")
                .withMobilePrivacy(MobilePrivacy.SHOW_ALL)
                .withOneTimeToken("new oneTimeToken")
                .withPassword("new password")
                .withRole(Role.ROLE_USER)
                .withPlayers(new Player(), new Player(), new Player());

        assertEquals(newVersion, existing.merge(newVersion));
        assertEquals(existing, existing.merge(new User()));
    }
}
