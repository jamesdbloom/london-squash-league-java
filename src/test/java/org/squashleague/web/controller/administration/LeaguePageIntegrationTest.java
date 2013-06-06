package org.squashleague.web.controller.administration;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.squashleague.domain.league.League;
import org.squashleague.web.controller.WebAndDataIntegrationTest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author jamesdbloom
 */
public class LeaguePageIntegrationTest extends WebAndDataIntegrationTest {

    private final static String OBJECT_NAME = "league";

    @Test
    public void shouldSaveLeagueWithNoErrors() throws Exception {
        assertNull(leagueDAO.findById(leagues.get(leagues.size() - 1).getId() + 1));

        // when
        mockMvc.perform(post("/" + OBJECT_NAME + "/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "test name")
                .param("club", clubs.get(0).getId().toString())
        )
                // then
                .andExpect(redirectedUrl("/administration"));

        leagueDAO.delete(leagues.get(leagues.size() - 1).getId() + 1);
    }

    @Test
    public void shouldSaveLeagueWithErrors() throws Exception {
        // when
        mockMvc.perform(post("/" + OBJECT_NAME + "/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                // then
                .andExpect(redirectedUrl("/administration#" + OBJECT_NAME + "s"))
                .andExpect(flash().attributeExists("bindingResult"))
                .andExpect(flash().attributeExists(OBJECT_NAME));
    }

    @Test
    public void shouldReturnPopulatedUpdateForm() throws Exception {
        // when
        MvcResult response = mockMvc.perform(get("/" + OBJECT_NAME + "/update/" + leagues.get(0).getId()).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        // then
        new LeagueUpdatePage(response).hasLeagueFields(leagues.get(0).getId(), leagues.get(0).getVersion(), leagues.get(0).getName(), leagues.get(0).getClub().getId());
    }

    @Test
    public void shouldUpdateLeagueNoErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "test name")
                .param("club", clubs.get(0).getId().toString())
        )
                .andExpect(redirectedUrl("/administration"));
    }

    @Test
    public void shouldGetPageWithClubError() throws Exception {
        // given
        League league = (League) new League()
                .withName("test name")
                .withId(2l);
        league.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", league.getId().toString())
                .param("version", league.getVersion().toString())
                .param("name", league.getName())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        LeagueUpdatePage leagueUpdatePage = new LeagueUpdatePage(response);
        leagueUpdatePage.hasErrors("league", 1);
        leagueUpdatePage.hasLeagueFields(league.getId(), league.getVersion(), league.getName(), null);
    }

    @Test
    public void shouldGetPageWithNameError() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", leagues.get(0).getId().toString())
                .param("version", leagues.get(0).getVersion().toString())
                .param("name", "four")
                .param("club", leagues.get(0).getClub().getId().toString())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        LeagueUpdatePage leagueUpdatePage = new LeagueUpdatePage(response);
        leagueUpdatePage.hasErrors("league", 1);
        leagueUpdatePage.hasLeagueFields(leagues.get(0).getId(), leagues.get(0).getVersion(), "four", leagues.get(0).getClub().getId());
    }

    @Test
    public void shouldGetPageWithAllErrors() throws Exception {
        // given
        League league = (League) new League()
                .withName("test")
                .withId(2l);
        league.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", league.getId().toString())
                .param("version", league.getVersion().toString())
                .param("name", league.getName())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        LeagueUpdatePage leagueUpdatePage = new LeagueUpdatePage(response);
        leagueUpdatePage.hasErrors("league", 2);
        leagueUpdatePage.hasLeagueFields(league.getId(), league.getVersion(), league.getName(), null);
    }

    @Test
    public void shouldDeleteLeague() throws Exception {
        // given
        League league = new League()
                .withName("to delete")
                .withClub(clubs.get(0));
        leagueDAO.save(league);
        assertNotNull(leagueDAO.findById(league.getId()));

        // when
        mockMvc.perform(get("/" + OBJECT_NAME + "/delete/" + league.getId())
                .accept(MediaType.TEXT_HTML)
        )
                // then
                .andExpect(redirectedUrl("/administration"));

        assertNull(leagueDAO.findById(league.getId()));
    }

}
