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
    public void getPage() throws Exception {
        mockMvc.perform(get("/administration").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    public void getPageWithRoleErrors() throws Exception {
        Role object = new Role()
                .withName("test name")
                .withDescription("test description");
        getAdministrationPage("role", 2, object).hasRoleFields("test name", "test description");
    }

    @Test
    public void getPageWithUserErrors() throws Exception {
        Role role = roleDAO.findAll().get(0);
        User object = new User()
                .withName("test name")
                .withEmail("test@email.com")
                .withMobile("123456789")
                .withMobilePrivacy(MobilePrivacy.SHOW_ALL)
                .withRole(role);
        getAdministrationPage("user", 2, object).hasUserFields("test name", "test@email.com", "123456789", MobilePrivacy.SHOW_ALL, role.getId());
    }

    @Test
    public void getPageWithClubErrors() throws Exception {
        Club object = new Club().withName("test name").withAddress("test address");
        getAdministrationPage("club", 2, object).hasClubFields("test name", "test address");
    }

    @Test
    public void getPageWithLeagueErrors() throws Exception {
        League object = new League().withName("test name");
        getAdministrationPage("league", 2, object).hasLeagueFields("test name");
    }

    @Test
    public void getPageWithDivisionErrors() throws Exception {
        Division object = new Division().withName("test name");
        getAdministrationPage("division", 2, object).hasDivisionFields("test name");
    }

    @Test
    public void getPageWithRoundErrors() throws Exception {
        Round object = new Round()
                .withDivision((Division) new Division().withId(1l))
                .withStartDate(new DateTime().plus(1))
                .withEndDate(new DateTime().plus(2));
        getAdministrationPage("round", 2, object).hasRoundFields(1l, new DateTime().plus(1).toString("yyyy-MM-dd"), new DateTime().plus(2).toString("yyyy-MM-dd"));
    }

    @Test
    public void getPageWithPlayerErrors() throws Exception {
        Player object = new Player()
                .withUser((User) new User().withId(1l))
                .withCurrentDivision((Division) new Division().withId(2l))
                .withPlayerStatus(PlayerStatus.ACTIVE);
        getAdministrationPage("player", 2, object).hasPlayerFields(1l, 2l, PlayerStatus.ACTIVE);
    }

    @Test
    public void getPageWithMatchErrors() throws Exception {
        Match object = new Match()
                .withRound((Round) new Round().withId(2l))
                .withPlayerOne((Player) new Player().withId(2l))
                .withPlayerTwo((Player) new Player().withId(1l))
                .withScore("4-5");
        getAdministrationPage("match", 2, object).hasMatchFields(2l, 2l, 1l, "4-5", new DateTime().toString("yyyy-MM-dd"));
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

        AdministrationPage loginPage = new AdministrationPage(response);
        loginPage.hasErrors(objectName, errorCount);
        return loginPage;
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
