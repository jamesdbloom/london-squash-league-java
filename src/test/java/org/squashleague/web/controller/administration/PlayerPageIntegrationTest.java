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
import org.squashleague.dao.account.UserDAO;
import org.squashleague.dao.league.DivisionDAO;
import org.squashleague.dao.league.LeagueDAO;
import org.squashleague.dao.league.PlayerDAO;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.*;
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
public class PlayerPageIntegrationTest {

    private final static String OBJECT_NAME = "player";
    @Resource
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Resource
    private PlayerDAO playerDAO;
    @Resource
    private DivisionDAO divisionDAO;
    @Resource
    private LeagueDAO leagueDAO;
    @Resource
    private UserDAO userDAO;
    private Division currentDivision;
    private User user;

    @Before
    public void setupFixture() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
        currentDivision = (Division) new Division()
                .withName("division one")
                .withLeague(
                        (League) new League()
                                .withName("league name")
                                .withClub(
                                        new Club()
                                                .withName("club name")
                                )
                                .withId(1l)
                )
                .withId(1l);
        when(divisionDAO.findById(currentDivision.getId())).thenReturn(currentDivision);
        when(divisionDAO.findAll()).thenReturn(
                Arrays.asList(
                        currentDivision,
                        (Division) new Division()
                                .withName("division two")
                                .withLeague(
                                        new League()
                                                .withName("league name")
                                                .withClub(
                                                        new Club()
                                                                .withName("club name")
                                                )
                                )
                                .withId(1l)
                )
        );

        when(leagueDAO.findById(currentDivision.getLeague().getId())).thenReturn(currentDivision.getLeague());
        when(leagueDAO.findAll()).thenReturn(
                Arrays.asList(
                        currentDivision.getLeague(),
                        (League) new League()
                                .withName("league name")
                                .withClub(
                                        new Club()
                                                .withName("club name")
                                )
                                .withId(1l)
                )
        );

        user = (User) new User()
                .withName("user one")
                .withId(1l);
        when(userDAO.findById(user.getId())).thenReturn(user);
        when(userDAO.findAll()).thenReturn(
                Arrays.asList(
                        user,
                        (User) new User()
                                .withName("user two")
                                .withId(3l)
                )
        );
    }

    @Test
    public void shouldSavePlayerWithNoErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("user", user.getId().toString())
                .param("currentDivision", currentDivision.getId().toString())
                .param("league", currentDivision.getLeague().getId().toString())
                .param("status", PlayerStatus.ACTIVE.name())
        )
                .andExpect(redirectedUrl("/administration"));
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
        Long id = 1l;
        Player player = (Player) new Player()
                .withUser(user)
                .withCurrentDivision(currentDivision)
                .withId(id);
        player.setVersion(5);
        when(playerDAO.findById(id)).thenReturn(player);

        MvcResult response = mockMvc.perform(get("/" + OBJECT_NAME + "/update/" + id).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        PlayerUpdatePage playerUpdatePage = new PlayerUpdatePage(response);
        playerUpdatePage.hasPlayerFields(player.getId(), player.getVersion(), player.getUser().getId(), player.getCurrentDivision().getId(), player.getLeague().getId(), player.getStatus());
    }

    @Test
    public void shouldUpdatePlayerNoErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("user", user.getId().toString())
                .param("currentDivision", currentDivision.getId().toString())
                .param("league", currentDivision.getLeague().getId().toString())
                .param("status", PlayerStatus.ACTIVE.name())
        )
                .andExpect(redirectedUrl("/administration"));
    }

    @Test
    public void shouldGetPageWithDivisionAndLeagueError() throws Exception {
        // given
        Player player = (Player) new Player()
                .withUser(user)
                .withStatus(PlayerStatus.ACTIVE)
                .withId(2l);
        player.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", player.getId().toString())
                .param("version", player.getVersion().toString())
                .param("user", player.getUser().getId().toString())
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
        playerUpdatePage.hasPlayerFields(player.getId(), player.getVersion(), player.getUser().getId(), null, null, player.getStatus());
    }

    @Test
    public void shouldGetPageWithUserError() throws Exception {
        // given
        Player player = (Player) new Player()
                .withUser(null)
                .withCurrentDivision(currentDivision)
                .withStatus(PlayerStatus.ACTIVE)
                .withId(2l);
        player.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", player.getId().toString())
                .param("version", player.getVersion().toString())
                .param("user", "")
                .param("currentDivision", currentDivision.getId().toString())
                .param("league", currentDivision.getLeague().getId().toString())
                .param("status", PlayerStatus.ACTIVE.name())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        PlayerUpdatePage playerUpdatePage = new PlayerUpdatePage(response);
        playerUpdatePage.hasErrors("player", 1);
        playerUpdatePage.hasPlayerFields(player.getId(), player.getVersion(), null, player.getCurrentDivision().getId(), player.getLeague().getId(), player.getStatus());
    }

    @Test
    public void shouldGetPageWithStatusError() throws Exception {
        // given
        Player player = (Player) new Player()
                .withUser(user)
                .withCurrentDivision(currentDivision)
                .withStatus(null)
                .withId(2l);
        player.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", player.getId().toString())
                .param("version", player.getVersion().toString())
                .param("user", player.getUser().getId().toString())
                .param("currentDivision", currentDivision.getId().toString())
                .param("league", currentDivision.getLeague().getId().toString())
                .param("status", "")
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        PlayerUpdatePage playerUpdatePage = new PlayerUpdatePage(response);
        playerUpdatePage.hasErrors("player", 1);
        playerUpdatePage.hasPlayerFields(player.getId(), player.getVersion(), player.getUser().getId(), player.getCurrentDivision().getId(), player.getLeague().getId(), player.getStatus());
    }

    @Test
    public void shouldGetPageWithAllErrors() throws Exception {
        // given
        Player player = (Player) new Player()
                .withId(2l);
        player.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", player.getId().toString())
                .param("version", player.getVersion().toString())
        )
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        PlayerUpdatePage playerUpdatePage = new PlayerUpdatePage(response);
        playerUpdatePage.hasErrors("player", 4);
        playerUpdatePage.hasPlayerFields(player.getId(), player.getVersion(), null, null, null, null);
    }

    @Test
    public void shouldDeletePlayer() throws Exception {
        // given
        Long id = 5l;

        // when
        mockMvc.perform(get("/" + OBJECT_NAME + "/delete/" + id)
                .accept(MediaType.TEXT_HTML)
        )
                // then
                .andExpect(redirectedUrl("/administration"));

        verify(playerDAO).delete(same(id));
    }

}
