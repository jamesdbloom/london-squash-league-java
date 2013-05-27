package org.squashleague.web.controller.administration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import org.squashleague.configuration.RootConfiguration;
import org.squashleague.dao.league.DivisionDAO;
import org.squashleague.dao.league.LeagueDAO;
import org.squashleague.domain.league.Division;
import org.squashleague.domain.league.League;
import org.squashleague.service.configuration.ServiceConfiguration;
import org.squashleague.web.configuration.WebMvcConfiguration;
import org.squashleague.web.controller.PropertyMockingApplicationContextInitializer;

import javax.annotation.Resource;
import java.util.Arrays;

import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author jamesdbloom
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration(
                name = "root",
                classes = RootConfiguration.class
        ),
        @ContextConfiguration(
                name = "dispatcher",
                classes = {WebMvcConfiguration.class},
                initializers = PropertyMockingApplicationContextInitializer.class
        )
})
public class DivisionPageIntegrationTest {

    private final static String OBJECT_NAME = "division";
    @Resource
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Resource
    private DivisionDAO divisionDAO;
    @Resource
    private LeagueDAO leagueDAO;
    private League league;

    @Before
    public void setupFixture() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
        league = (League) new League()
                .withName("league one")
                .withId(1l);
        when(leagueDAO.findById(league.getId())).thenReturn(league);
        when(leagueDAO.findAll()).thenReturn(Arrays.asList(league, (League) new League().withName("league two").withId(2l)));
    }

    @Test
    public void shouldSaveDivisionWithNoErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "test name")
                .param("league", league.getId().toString())
        )
                .andExpect(redirectedUrl("/administration"));
    }

    @Test
    public void shouldSaveDivisionWithErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(redirectedUrl("/administration#" + OBJECT_NAME + "s"))
                .andExpect(flash().attributeExists("bindingResult"))
                .andExpect(flash().attributeExists(OBJECT_NAME));
    }

    @Test
    public void shouldReturnPopulatedUpdateForm() throws Exception {
        Long id = 1l;
        Division division = (Division) new Division()
                .withName("test name")
                .withLeague(league)
                .withId(id);
        division.setVersion(5);
        when(divisionDAO.findById(id)).thenReturn(division);


        MvcResult response = mockMvc.perform(get("/" + OBJECT_NAME + "/update/" + id).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        DivisionUpdatePage DivisionUpdatePage = new DivisionUpdatePage(response);
        DivisionUpdatePage.hasDivisionFields(division.getId(), division.getVersion(), division.getName(), division.getLeague().getId());
    }

    @Test
    public void shouldUpdateDivisionNoErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "test name")
                .param("league", league.getId().toString())
        )
                .andExpect(redirectedUrl("/administration"));
    }

    @Test
    public void shouldGetPageWithLeagueError() throws Exception {
        // given
        Division division = (Division) new Division()
                .withName("test name")
                .withId(2l);
        division.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", division.getId().toString())
                .param("version", division.getVersion().toString())
                .param("name", division.getName())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        DivisionUpdatePage DivisionUpdatePage = new DivisionUpdatePage(response);
        DivisionUpdatePage.hasErrors("division", 1);
        DivisionUpdatePage.hasDivisionFields(division.getId(), division.getVersion(), division.getName(), null);
    }

    @Test
    public void shouldGetPageWithNameError() throws Exception {
        // given
        Division division = (Division) new Division()
                .withName("")
                .withLeague(league)
                .withId(2l);
        division.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", division.getId().toString())
                .param("version", division.getVersion().toString())
                .param("name", division.getName())
                .param("league", division.getLeague().getId().toString())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        DivisionUpdatePage DivisionUpdatePage = new DivisionUpdatePage(response);
        DivisionUpdatePage.hasErrors("division", 1);
        DivisionUpdatePage.hasDivisionFields(division.getId(), division.getVersion(), division.getName(), division.getLeague().getId());
    }

    @Test
    public void shouldGetPageWithAllErrors() throws Exception {
        // given
        Division division = (Division) new Division()
                .withName("test")
                .withId(2l);
        division.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", division.getId().toString())
                .param("version", division.getVersion().toString())
                .param("name", division.getName())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        DivisionUpdatePage DivisionUpdatePage = new DivisionUpdatePage(response);
        DivisionUpdatePage.hasErrors("division", 2);
        DivisionUpdatePage.hasDivisionFields(division.getId(), division.getVersion(), division.getName(), null);
    }

    @Test
    public void shouldDeleteDivision() throws Exception {
        // given
        Long id = 5l;

        // when
        mockMvc.perform(get("/" + OBJECT_NAME + "/delete/" + id)
                .accept(MediaType.TEXT_HTML)
        )
                // then
                .andExpect(redirectedUrl("/administration"));

        verify(divisionDAO).delete(same(id));
    }

}
