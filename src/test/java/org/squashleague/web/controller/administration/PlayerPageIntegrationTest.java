package org.squashleague.web.controller.administration;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.squashleague.domain.league.Player;
import org.squashleague.domain.league.PlayerStatus;
import org.squashleague.web.controller.WebAndDataIntegrationTest;

import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author jamesdbloom
 */
public class PlayerPageIntegrationTest extends WebAndDataIntegrationTest {

    private final static String OBJECT_NAME = "player";

    @Test
    public void shouldSavePlayerWithNoErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("user", userOne.getId().toString())
                .param("league", leagueTwo.getId().toString())
                .param("status", PlayerStatus.ACTIVE.name())
        )
                .andExpect(redirectedUrl("/administration"));

        playerDAO.delete(playerThree.getId() + 1);
    }

    @Test
    public void shouldSavePlayerWithErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(redirectedUrl("/administration#" + OBJECT_NAME + "s"))
                .andExpect(flash().attributeExists("bindingResult"))
                .andExpect(flash().attributeExists(OBJECT_NAME));
    }

    @Test
    public void shouldReturnPopulatedUpdateForm() throws Exception {
        MvcResult response = mockMvc.perform(get("/" + OBJECT_NAME + "/update/" + playerOne.getId()).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        PlayerUpdatePage playerUpdatePage = new PlayerUpdatePage(response);
        playerUpdatePage.hasPlayerFields(playerOne.getId(), playerOne.getVersion(), playerOne.getUser().getId(), playerOne.getCurrentDivision().getId(), playerOne.getLeague().getId(), playerOne.getStatus());
    }

    @Test
    public void shouldUpdatePlayerNoErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("user", userOne.getId().toString())
                .param("currentDivision", division.getId().toString())
                .param("league", division.getLeague().getId().toString())
                .param("status", PlayerStatus.ACTIVE.name())
        )
                .andExpect(redirectedUrl("/administration"));
    }

    @Test
    public void shouldGetPageWithDivisionAndLeagueError() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", playerOne.getId().toString())
                .param("version", playerOne.getVersion().toString())
                .param("user", playerOne.getUser().getId().toString())
                .param("currentDivision", "")
                .param("league", "")
                .param("status", PlayerStatus.ACTIVE.name())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        PlayerUpdatePage playerUpdatePage = new PlayerUpdatePage(response);
        playerUpdatePage.hasErrors("player", 2);
        playerUpdatePage.hasPlayerFields(playerOne.getId(), playerOne.getVersion(), playerOne.getUser().getId(), null, null, playerOne.getStatus());
    }

    @Test
    public void shouldGetPageWithUserError() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", playerOne.getId().toString())
                .param("version", playerOne.getVersion().toString())
                .param("user", "")
                .param("currentDivision", division.getId().toString())
                .param("league", division.getLeague().getId().toString())
                .param("status", PlayerStatus.ACTIVE.name())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        PlayerUpdatePage playerUpdatePage = new PlayerUpdatePage(response);
        playerUpdatePage.hasErrors("player", 1);
        playerUpdatePage.hasPlayerFields(playerOne.getId(), playerOne.getVersion(), null, playerOne.getCurrentDivision().getId(), playerOne.getLeague().getId(), playerOne.getStatus());
    }

    @Test
    public void shouldGetPageWithStatusError() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", playerOne.getId().toString())
                .param("version", playerOne.getVersion().toString())
                .param("user", playerOne.getUser().getId().toString())
                .param("currentDivision", division.getId().toString())
                .param("league", division.getLeague().getId().toString())
                .param("status", "")
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        PlayerUpdatePage playerUpdatePage = new PlayerUpdatePage(response);
        playerUpdatePage.hasErrors("player", 1);
        playerUpdatePage.hasPlayerFields(playerOne.getId(), playerOne.getVersion(), playerOne.getUser().getId(), playerOne.getCurrentDivision().getId(), playerOne.getLeague().getId(), null);
    }

    @Test
    public void shouldGetPageWithAllErrors() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", playerOne.getId().toString())
                .param("version", playerOne.getVersion().toString())
        )
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        PlayerUpdatePage playerUpdatePage = new PlayerUpdatePage(response);
        playerUpdatePage.hasErrors("player", 3);
        playerUpdatePage.hasPlayerFields(playerOne.getId(), playerOne.getVersion(), null, null, null, null);
    }

    @Test
    public void shouldDeletePlayer() throws Exception {
        // given
        Player player = new Player()
                .withUser(userOne)
                .withStatus(PlayerStatus.ACTIVE)
                .withLeague(leagueTwo);
        playerDAO.save(player);

        // when
        mockMvc.perform(get("/" + OBJECT_NAME + "/delete/" + player.getId())
                .accept(MediaType.TEXT_HTML)
        )
                // then
                .andExpect(redirectedUrl("/administration"));

        assertNull(playerDAO.findById(player.getId()));
    }

}
