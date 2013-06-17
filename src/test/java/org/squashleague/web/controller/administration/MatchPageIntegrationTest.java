package org.squashleague.web.controller.administration;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.squashleague.domain.league.Match;
import org.squashleague.web.controller.WebAndDataIntegrationTest;

import static org.junit.Assert.assertNotNull;
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
        assertNull(matchDAO.findById(matches.get(matches.size() - 1).getId() + 1));

        mockMvc.perform(post("/" + OBJECT_NAME + "/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("playerOne", players.get(0).getId().toString())
                .param("playerTwo", players.get(1).getId().toString())
                .param("division", divisions.get(0).getId().toString())
        )
                .andExpect(redirectedUrl("/administration"));

        matchDAO.delete(matches.get(matches.size() - 1).getId() + 1);
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
        MvcResult response = mockMvc.perform(get("/" + OBJECT_NAME + "/update/" + matches.get(0).getId()).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        MatchUpdatePage MatchUpdatePage = new MatchUpdatePage(response);
        MatchUpdatePage.hasMatchFields(matches.get(0).getId(), matches.get(0).getVersion(), matches.get(0).getPlayerOne().getId(), matches.get(0).getPlayerTwo().getId(), matches.get(0).getDivision().getId());
    }

    @Test
    public void shouldUpdateMatchNoErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("playerOne", players.get(0).getId().toString())
                .param("playerTwo", players.get(1).getId().toString())
                .param("division", divisions.get(0).getId().toString())
        )
                .andExpect(redirectedUrl("/administration"));
    }

    @Test
    public void shouldGetPageWithRoundError() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", matches.get(0).getId().toString())
                .param("version", matches.get(0).getVersion().toString())
                .param("playerOne", matches.get(0).getPlayerOne().getId().toString())
                .param("playerTwo", matches.get(0).getPlayerTwo().getId().toString())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        MatchUpdatePage MatchUpdatePage = new MatchUpdatePage(response);
        MatchUpdatePage.hasErrors("match", 1);
        MatchUpdatePage.hasMatchFields(matches.get(0).getId(), matches.get(0).getVersion(), matches.get(0).getPlayerOne().getId(), matches.get(0).getPlayerTwo().getId(), null);
    }

    @Test
    public void shouldGetPageWithPlayerOneError() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", matches.get(0).getId().toString())
                .param("version", matches.get(0).getVersion().toString())
                .param("playerOne", "")
                .param("playerTwo", matches.get(0).getPlayerTwo().getId().toString())
                .param("division", matches.get(0).getDivision().getId().toString())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        MatchUpdatePage MatchUpdatePage = new MatchUpdatePage(response);
        MatchUpdatePage.hasErrors("match", 1);
        MatchUpdatePage.hasMatchFields(matches.get(0).getId(), matches.get(0).getVersion(), null, matches.get(0).getPlayerTwo().getId(), matches.get(0).getDivision().getId());
    }

    @Test
    public void shouldGetPageWithPlayerTwoError() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", matches.get(0).getId().toString())
                .param("version", matches.get(0).getVersion().toString())
                .param("playerOne", matches.get(0).getPlayerOne().getId().toString())
                .param("playerTwo", "")
                .param("division", matches.get(0).getDivision().getId().toString())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        MatchUpdatePage MatchUpdatePage = new MatchUpdatePage(response);
        MatchUpdatePage.hasErrors("match", 1);
        MatchUpdatePage.hasMatchFields(matches.get(0).getId(), matches.get(0).getVersion(), matches.get(0).getPlayerOne().getId(), null, matches.get(0).getDivision().getId());
    }

    @Test
    public void shouldGetPageWithPlayersIdenticalError() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", matches.get(0).getId().toString())
                .param("version", matches.get(0).getVersion().toString())
                .param("playerOne", matches.get(0).getPlayerOne().getId().toString())
                .param("playerTwo", matches.get(0).getPlayerOne().getId().toString())
                .param("division", matches.get(0).getDivision().getId().toString())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        MatchUpdatePage MatchUpdatePage = new MatchUpdatePage(response);
        MatchUpdatePage.hasErrors("match", 1);
        MatchUpdatePage.hasMatchFields(matches.get(0).getId(), matches.get(0).getVersion(), matches.get(0).getPlayerOne().getId(), matches.get(0).getPlayerOne().getId(), matches.get(0).getDivision().getId());
    }

    @Test
    public void shouldGetPageWithAllErrors() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", matches.get(0).getId().toString())
                .param("version", matches.get(0).getVersion().toString())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        MatchUpdatePage MatchUpdatePage = new MatchUpdatePage(response);
        MatchUpdatePage.hasErrors("match", 3);
        MatchUpdatePage.hasMatchFields(matches.get(0).getId(), matches.get(0).getVersion(), null, null, null);
    }

    @Test
    public void shouldDeleteMatch() throws Exception {
        // given
        Match match = new Match()
                .withPlayerOne(players.get(0))
                .withPlayerTwo(players.get(1))
                .withDivision(divisions.get(0));
        matchDAO.save(match);
        assertNotNull(matchDAO.findById(match.getId()));

        // when
        mockMvc.perform(get("/" + OBJECT_NAME + "/delete/" + match.getId())
                .accept(MediaType.TEXT_HTML)
        )
                // then
                .andExpect(redirectedUrl("/administration"));

        assertNull(matchDAO.findById(match.getId()));
    }

}
