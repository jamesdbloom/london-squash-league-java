package org.squashleague.web.controller.administration;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.squashleague.domain.league.Match;
import org.squashleague.web.controller.WebAndDataIntegrationTest;

import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author jamesdbloom
 */
public class MatchPageIntegrationTest extends WebAndDataIntegrationTest {

    private final static String OBJECT_NAME = "match";

    @Test
    public void shouldSaveMatchWithNoErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("playerOne", playerOne.getId().toString())
                .param("playerTwo", playerTwo.getId().toString())
                .param("round", round.getId().toString())
        )
                .andExpect(redirectedUrl("/administration"));

        matchDAO.delete(matchFour.getId() + 1);
    }

    @Test
    public void shouldSaveMatchWithErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(redirectedUrl("/administration#" + OBJECT_NAME + "es"))
                .andExpect(flash().attributeExists("bindingResult"))
                .andExpect(flash().attributeExists(OBJECT_NAME));
    }

    @Test
    public void shouldReturnPopulatedUpdateForm() throws Exception {
        MvcResult response = mockMvc.perform(get("/" + OBJECT_NAME + "/update/" + matchOne.getId()).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        MatchUpdatePage MatchUpdatePage = new MatchUpdatePage(response);
        MatchUpdatePage.hasMatchFields(matchOne.getId(), matchOne.getVersion(), matchOne.getPlayerOne().getId(), matchOne.getPlayerTwo().getId(), matchOne.getRound().getId());
    }

    @Test
    public void shouldUpdateMatchNoErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("playerOne", playerOne.getId().toString())
                .param("playerTwo", playerTwo.getId().toString())
                .param("round", round.getId().toString())
        )
                .andExpect(redirectedUrl("/administration"));
    }

    @Test
    public void shouldGetPageWithRoundError() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", matchOne.getId().toString())
                .param("version", matchOne.getVersion().toString())
                .param("playerOne", matchOne.getPlayerOne().getId().toString())
                .param("playerTwo", matchOne.getPlayerTwo().getId().toString())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        MatchUpdatePage MatchUpdatePage = new MatchUpdatePage(response);
        MatchUpdatePage.hasErrors("match", 1);
        MatchUpdatePage.hasMatchFields(matchOne.getId(), matchOne.getVersion(), matchOne.getPlayerOne().getId(), matchOne.getPlayerTwo().getId(), null);
    }

    @Test
    public void shouldGetPageWithPlayerOneError() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", matchOne.getId().toString())
                .param("version", matchOne.getVersion().toString())
                .param("playerOne", "")
                .param("playerTwo", matchOne.getPlayerTwo().getId().toString())
                .param("round", matchOne.getRound().getId().toString())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        MatchUpdatePage MatchUpdatePage = new MatchUpdatePage(response);
        MatchUpdatePage.hasErrors("match", 1);
        MatchUpdatePage.hasMatchFields(matchOne.getId(), matchOne.getVersion(), null, matchOne.getPlayerTwo().getId(), matchOne.getRound().getId());
    }

    @Test
    public void shouldGetPageWithPlayerTwoError() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", matchOne.getId().toString())
                .param("version", matchOne.getVersion().toString())
                .param("playerOne", matchOne.getPlayerOne().getId().toString())
                .param("playerTwo", "")
                .param("round", matchOne.getRound().getId().toString())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        MatchUpdatePage MatchUpdatePage = new MatchUpdatePage(response);
        MatchUpdatePage.hasErrors("match", 1);
        MatchUpdatePage.hasMatchFields(matchOne.getId(), matchOne.getVersion(), matchOne.getPlayerOne().getId(), null, matchOne.getRound().getId());
    }

    @Test
    public void shouldGetPageWithPlayersIdenticalError() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", matchOne.getId().toString())
                .param("version", matchOne.getVersion().toString())
                .param("playerOne", matchOne.getPlayerOne().getId().toString())
                .param("playerTwo", matchOne.getPlayerOne().getId().toString())
                .param("round", matchOne.getRound().getId().toString())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        MatchUpdatePage MatchUpdatePage = new MatchUpdatePage(response);
        MatchUpdatePage.hasErrors("match", 1);
        MatchUpdatePage.hasMatchFields(matchOne.getId(), matchOne.getVersion(), matchOne.getPlayerOne().getId(), matchOne.getPlayerOne().getId(), matchOne.getRound().getId());
    }

    @Test
    public void shouldGetPageWithAllErrors() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", matchOne.getId().toString())
                .param("version", matchOne.getVersion().toString())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        MatchUpdatePage MatchUpdatePage = new MatchUpdatePage(response);
        MatchUpdatePage.hasErrors("match", 3);
        MatchUpdatePage.hasMatchFields(matchOne.getId(), matchOne.getVersion(), null, null, null);
    }

    @Test
    public void shouldDeleteMatch() throws Exception {
        // given
        Match match = new Match()
                .withPlayerOne(playerOne)
                .withPlayerTwo(playerTwo)
                .withRound(round);
        matchDAO.save(match);

        // when
        mockMvc.perform(get("/" + OBJECT_NAME + "/delete/" + match.getId())
                .accept(MediaType.TEXT_HTML)
        )
                // then
                .andExpect(redirectedUrl("/administration"));

        assertNull(matchDAO.findById(match.getId()));
    }

}
