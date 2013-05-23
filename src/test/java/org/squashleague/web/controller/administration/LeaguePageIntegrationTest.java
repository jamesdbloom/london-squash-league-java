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
import org.squashleague.dao.league.ClubDAO;
import org.squashleague.dao.league.LeagueDAO;
import org.squashleague.domain.league.Club;
import org.squashleague.domain.league.League;
import org.squashleague.web.configuration.WebMvcConfiguration;
import org.squashleague.web.controller.PropertyMockingApplicationContextInitializer;

import javax.annotation.Resource;
import java.util.Arrays;

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
                classes = WebMvcConfiguration.class,
                initializers = PropertyMockingApplicationContextInitializer.class
        )
})
public class LeaguePageIntegrationTest {

    private final static String OBJECT_NAME = "league";
    @Resource
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Resource
    private LeagueDAO leagueDAO;
    @Resource
    private ClubDAO clubDAO;
    private Club club;

    @Before
    public void setupFixture() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
        club = (Club) new Club()
                .withName("club one")
                .withId(1l);
        when(clubDAO.findById(club.getId())).thenReturn(club);
        when(clubDAO.findAll()).thenReturn(Arrays.asList(club, (Club) new Club().withName("club two").withId(2l)));
    }

    @Test
    public void shouldSaveLeagueWithNoErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "test name")
                .param("club", club.getId().toString())
        )
                .andExpect(redirectedUrl("/administration"));
    }

    @Test
    public void shouldSaveLeagueWithErrors() throws Exception {
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
        League object = (League) new League()
                .withName("test name")
                .withClub(club)
                .withId(id);
        object.setVersion(5);
        when(leagueDAO.findById(id)).thenReturn(object);


        MvcResult response = mockMvc.perform(get("/" + OBJECT_NAME + "/update/" + id).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        LeagueUpdatePage leagueUpdatePage = new LeagueUpdatePage(response);
        leagueUpdatePage.hasLeagueFields(object.getId(), object.getVersion(), object.getName(), object.getClub().getId());
    }

    @Test
    public void shouldUpdateLeagueNoErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "test name")
                .param("club", club.getId().toString())
        )
                .andExpect(redirectedUrl("/administration"));
    }

    @Test
    public void shouldGetPageWithClubError() throws Exception {
        // given
        League object = (League) new League()
                .withName("test name")
                .withId(2l);
        object.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", object.getId().toString())
                .param("version", object.getVersion().toString())
                .param("name", object.getName())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        LeagueUpdatePage leagueUpdatePage = new LeagueUpdatePage(response);
        leagueUpdatePage.hasErrors("league", 1);
        leagueUpdatePage.hasLeagueFields(object.getId(), object.getVersion(), object.getName(), null);
    }

    @Test
    public void shouldGetPageWithNameError() throws Exception {
        // given
        League object = (League) new League()
                .withName("")
                .withClub(club)
                .withId(2l);
        object.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", object.getId().toString())
                .param("version", object.getVersion().toString())
                .param("name", object.getName())
                .param("club", object.getClub().getId().toString())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        LeagueUpdatePage leagueUpdatePage = new LeagueUpdatePage(response);
        leagueUpdatePage.hasErrors("league", 1);
        leagueUpdatePage.hasLeagueFields(object.getId(), object.getVersion(), object.getName(), object.getClub().getId());
    }

    @Test
    public void shouldGetPageWithAllErrors() throws Exception {
        // given
        League object = (League) new League()
                .withName("test")
                .withId(2l);
        object.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", object.getId().toString())
                .param("version", object.getVersion().toString())
                .param("name", object.getName())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        LeagueUpdatePage leagueUpdatePage = new LeagueUpdatePage(response);
        leagueUpdatePage.hasErrors("league", 2);
        leagueUpdatePage.hasLeagueFields(object.getId(), object.getVersion(), object.getName(), null);
    }

}
