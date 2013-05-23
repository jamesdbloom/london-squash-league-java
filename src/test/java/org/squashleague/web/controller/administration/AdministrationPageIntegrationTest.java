package org.squashleague.web.controller.administration;

import org.joda.time.DateTime;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.WebApplicationContext;
import org.squashleague.configuration.RootConfiguration;
import org.squashleague.dao.account.RoleDAO;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.*;
import org.squashleague.web.configuration.WebMvcConfiguration;
import org.squashleague.web.controller.PropertyMockingApplicationContextInitializer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
                classes = {WebMvcConfiguration.class, MockDAOConfiguration.class},
                initializers = PropertyMockingApplicationContextInitializer.class
        )
})
public class AdministrationPageIntegrationTest {

    @Resource
    private RoleDAO roleDAO;
    @Resource
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setupFixture() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldGetPage() throws Exception {
        mockMvc.perform(get("/administration").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    public void shouldGetPageWithRoleErrors() throws Exception {
        Role role = new Role()
                .withName("test name")
                .withDescription("test description");
        getAdministrationPage("role", 2, role).hasRoleFields(role.getName(), role.getDescription());
    }

    @Test
    public void shouldGetPageWithUserErrors() throws Exception {
        Role role = roleDAO.findAll().get(0);
        User user = new User()
                .withName("test name")
                .withEmail("test@email.com")
                .withMobile("123456789")
                .withMobilePrivacy(MobilePrivacy.SHOW_ALL)
                .withRole(role);
        getAdministrationPage("user", 2, user).hasUserFields(user.getName(), user.getEmail(), user.getMobile(), user.getMobilePrivacy(), role.getName());
    }

    @Test
    public void shouldGetPageWithClubErrors() throws Exception {
        Club object = new Club().withName("test name").withAddress("test address");
        getAdministrationPage("club", 2, object).hasClubFields("test name", "test address");
    }

    @Test
    public void shouldGetPageWithLeagueErrors() throws Exception {
        League league = new League()
                .withName("test name");
        getAdministrationPage("league", 2, league).hasLeagueFields(league.getName());
    }

    @Test
    public void shouldGetPageWithDivisionErrors() throws Exception {
        Division division = new Division()
                .withName("test name");
        getAdministrationPage("division", 2, division).hasDivisionFields(division.getName());
    }

    @Test
    public void shouldGetPageWithRoundErrors() throws Exception {
        Round round = new Round()
                .withDivision((Division) new Division().withId(1l))
                .withStartDate(new DateTime().plus(1))
                .withEndDate(new DateTime().plus(2));
        getAdministrationPage("round", 2, round).hasRoundFields(round.getDivision().getId(), round.getStartDate(), round.getEndDate());
    }

    @Test
    public void shouldGetPageWithPlayerErrors() throws Exception {
        Player player = new Player()
                .withUser((User) new User().withId(1l))
                .withCurrentDivision((Division) new Division().withId(2l))
                .withPlayerStatus(PlayerStatus.ACTIVE);
        getAdministrationPage("player", 2, player).hasPlayerFields(player.getUser().getId(), player.getCurrentDivision().getId(), player.getStatus());
    }

    @Test
    public void shouldGetPageWithMatchErrors() throws Exception {
        Match match = new Match()
                .withRound((Round) new Round().withId(2l))
                .withPlayerOne((Player) new Player().withId(2l))
                .withPlayerTwo((Player) new Player().withId(1l))
                .withScore("4-5");
        getAdministrationPage("match", 2, match).hasMatchFields(match.getRound().getId(), match.getPlayerOne().getId(), match.getPlayerTwo().getId(), match.getScore());
    }

    private AdministrationPage getAdministrationPage(String objectName, int errorCount, Object object) throws Exception {
        MvcResult response = mockMvc.perform(get("/administration")
                .accept(MediaType.TEXT_HTML)
                .flashAttr("bindingResult", getBindingResult(objectName, errorCount))
                .flashAttr(objectName, object)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        AdministrationPage administrationPage = new AdministrationPage(response);
        administrationPage.hasErrors(objectName, errorCount);
        return administrationPage;
    }

    private BindingResult getBindingResult(String objectName, int errorCount) {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getObjectName()).thenReturn(objectName);
        List<ObjectError> objectErrors = new ArrayList<>();
        for (int i = 0; i < errorCount; i++) {
            objectErrors.add(i, mock(ObjectError.class));
            when(objectErrors.get(i).getDefaultMessage()).thenReturn("test message " + i);
        }
        when(bindingResult.getAllErrors()).thenReturn(objectErrors);
        return bindingResult;
    }

}
