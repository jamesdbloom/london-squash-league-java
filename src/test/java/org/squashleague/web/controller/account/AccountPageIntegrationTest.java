package org.squashleague.web.controller.account;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.squashleague.domain.league.Match;
import org.squashleague.domain.league.Player;
import org.squashleague.domain.league.PlayerStatus;
import org.squashleague.web.controller.WebAndDataIntegrationTest;

import java.util.Arrays;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author jamesdbloom
 */
public class AccountPageIntegrationTest extends WebAndDataIntegrationTest {

    @Test
    public void shouldDisplayUserDetails() throws Exception {
        securityUserContext.setCurrentUser(userOne);

        try {
            MvcResult result = mockMvc.perform(get("/account").accept(MediaType.TEXT_HTML))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andReturn();

            AccountPage accountPage = new AccountPage(result);
            accountPage.hasUserDetails(userOne);
            accountPage.hasPlayers(Arrays.asList(leagueThree), playerOne, playerFour);
            accountPage.hasRounds(roundOne, roundTwo);
            accountPage.hasMatches(new Match[][]{{matchOne, matchTwo, matchFive}, {matchFive}}, userOne);
        } finally {
            securityUserContext.setCurrentUser(LOGGED_IN_USER);
        }
    }

    @Test
    public void shouldRegisterPlayer() throws Exception {
        securityUserContext.setCurrentUser(userThree);

        try {
            assertEquals(PlayerStatus.INACTIVE, playerDAO.findById(playerThree.getId()).getStatus());

            mockMvc.perform(get("/account/register")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("player", playerThree.getId().toString())
            )
                    // then
                    .andExpect(redirectedUrl("/account"));

            assertEquals(PlayerStatus.ACTIVE, playerDAO.findById(playerThree.getId()).getStatus());
        } finally {
            playerDAO.updateStatus(playerThree, PlayerStatus.INACTIVE);
            securityUserContext.setCurrentUser(LOGGED_IN_USER);
        }
    }

    @Test
    public void shouldUnregisterPlayer() throws Exception {
        securityUserContext.setCurrentUser(userOne);

        try {
            assertEquals(PlayerStatus.ACTIVE, playerDAO.findById(playerOne.getId()).getStatus());

            mockMvc.perform(get("/account/unregister")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("player", playerOne.getId().toString())
            )
                    // then
                    .andExpect(redirectedUrl("/account"));

            assertEquals(PlayerStatus.INACTIVE, playerDAO.findById(playerOne.getId()).getStatus());
        } finally {
            playerDAO.updateStatus(playerOne, PlayerStatus.ACTIVE);
            securityUserContext.setCurrentUser(LOGGED_IN_USER);
        }
    }

    @Test
    public void shouldRegisterLeague() throws Exception {
        securityUserContext.setCurrentUser(userOne);

        try {
            assertNull(playerDAO.findById(playerFour.getId() + 1));

            mockMvc.perform(post("/account/register")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("league", leagueThree.getId().toString())
            )
                    // then
                    .andExpect(redirectedUrl("/account"));

            Player newPlayer = playerDAO.findById(playerFour.getId() + 1);
            assertEquals(PlayerStatus.ACTIVE, newPlayer.getStatus());
            assertEquals(leagueThree.getId(), newPlayer.getLeague().getId());
            assertEquals(userOne.getId(), newPlayer.getUser().getId());
        } finally {
            playerDAO.delete(playerFour.getId() + 1);
            securityUserContext.setCurrentUser(LOGGED_IN_USER);
        }
    }
}
