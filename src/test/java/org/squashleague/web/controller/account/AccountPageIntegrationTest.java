package org.squashleague.web.controller.account;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.Player;
import org.squashleague.domain.league.PlayerStatus;
import org.squashleague.web.controller.WebAndDataIntegrationTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author jamesdbloom
 */
public class AccountPageIntegrationTest extends WebAndDataIntegrationTest {

    @Test
    public void shouldDisplayAccountPage() throws Exception {
        User user = users.get(0);
        securityUserContext.setCurrentUser(user);

        try {
            MvcResult result = mockMvc.perform(get("/account").accept(MediaType.TEXT_HTML))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andReturn();

            AccountPage accountPage = new AccountPage(result);
            accountPage.hasUserDetails(user);
            accountPage.hasPlayers(leaguesNoPlayers, playersUserBothDivisions);
            accountPage.hasRounds(rounds);
            accountPage.hasMatches(user, getUserMatches(addLists(matchesDivisionZeroRoundZero, matchesDivisionZeroRoundOne), user), getUserMatches(addLists(matchesDivisionOneRoundZero, matchesDivisionOneRoundOne), user));
        } finally {
            securityUserContext.setCurrentUser(LOGGED_IN_USER);
        }
    }

    @Test
    public void shouldRegisterPlayer() throws Exception {
        securityUserContext.setCurrentUser(users.get(0));

        try {
            playerDAO.updateStatus(players.get(0), PlayerStatus.INACTIVE);
            assertEquals(PlayerStatus.INACTIVE, playerDAO.findById(players.get(0).getId()).getStatus());

            mockMvc.perform(get("/account/register")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("player", players.get(0).getId().toString())
            )
                    // then
                    .andExpect(redirectedUrl("/account"));

            assertEquals(PlayerStatus.ACTIVE, playerDAO.findById(players.get(0).getId()).getStatus());
        } finally {
            securityUserContext.setCurrentUser(LOGGED_IN_USER);
        }
    }

    @Test
    public void shouldUnregisterPlayer() throws Exception {
        securityUserContext.setCurrentUser(users.get(0));

        try {
            assertEquals(PlayerStatus.ACTIVE, playerDAO.findById(players.get(0).getId()).getStatus());

            mockMvc.perform(get("/account/unregister")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("player", players.get(0).getId().toString())
            )
                    // then
                    .andExpect(redirectedUrl("/account"));

            assertEquals(PlayerStatus.INACTIVE, playerDAO.findById(players.get(0).getId()).getStatus());
        } finally {
            playerDAO.updateStatus(players.get(0), PlayerStatus.ACTIVE);
            securityUserContext.setCurrentUser(LOGGED_IN_USER);
        }
    }

    @Test
    public void shouldRegisterLeague() throws Exception {
        securityUserContext.setCurrentUser(users.get(users.size() - 1));

        try {
            assertNull(playerDAO.findById(players.get(players.size() - 1).getId() + 1));

            mockMvc.perform(post("/account/register")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("league", leagues.get(2).getId().toString())
            )
                    // then
                    .andExpect(redirectedUrl("/account"));

            Player newPlayer = playerDAO.findById(players.get(players.size() - 1).getId() + 1);
            assertEquals(PlayerStatus.ACTIVE, newPlayer.getStatus());
            assertEquals(leagues.get(leagues.size() - 1).getId(), newPlayer.getLeague().getId());
            assertEquals(users.get(users.size() - 1).getId(), newPlayer.getUser().getId());
        } finally {
            playerDAO.delete(players.get(players.size() - 1).getId() + 1);
            securityUserContext.setCurrentUser(LOGGED_IN_USER);
        }
    }
}
