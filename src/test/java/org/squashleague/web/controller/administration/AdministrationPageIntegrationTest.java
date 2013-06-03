package org.squashleague.web.controller.administration;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.orm.hibernate3.support.OpenSessionInViewFilter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.WebApplicationContext;
import org.squashleague.configuration.RootConfiguration;
import org.squashleague.dao.account.RoleDAO;
import org.squashleague.dao.league.HSQLApplicationContextInitializer;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;
import org.squashleague.domain.league.*;
import org.squashleague.service.security.SecurityMockingConfiguration;
import org.squashleague.web.configuration.WebMvcConfiguration;
import org.squashleague.web.controller.PropertyMockingApplicationContextInitializer;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.ServletException;
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
                classes = RootConfiguration.class,
                initializers = HSQLApplicationContextInitializer.class
        ),
        @ContextConfiguration(
                name = "dispatcher",
                classes = WebMvcConfiguration.class,
                initializers = PropertyMockingApplicationContextInitializer.class
        )
})
public class AdministrationPageIntegrationTest extends MockDAOTest {

    @Resource
    private RoleDAO roleDAO;
    @Resource
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setupFixture() throws ServletException {
        OpenEntityManagerInViewFilter openSessionInViewFilter = new OpenEntityManagerInViewFilter();
        openSessionInViewFilter.setPersistenceUnitName("persistenceUnit");
        openSessionInViewFilter.init(new MockFilterConfig(webApplicationContext.getServletContext(), "openSessionInViewFilter"));

        mockMvc = webAppContextSetup(webApplicationContext).addFilters(openSessionInViewFilter).build();
    }

    @Test
    public void shouldGetPage() throws Exception {
        mockMvc.perform(get("/administration").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    public void shouldGetPageWithRoleErrors() throws Exception {
        getAdministrationPage("role", 2, role).hasRoleFields(role.getName(), role.getDescription());
    }

    @Test
    public void shouldGetPageWithUserErrors() throws Exception {
        getAdministrationPage("user", 2, userOne).hasUserFields(userOne.getName(), userOne.getEmail(), userOne.getMobile(), userOne.getMobilePrivacy(), userOne.getRoleNames());
    }

    @Test
    public void shouldGetPageWithClubErrors() throws Exception {
        getAdministrationPage("club", 2, club).hasClubFields(club.getName(), club.getAddress());
    }

    @Test
    public void shouldGetPageWithLeagueErrors() throws Exception {
        getAdministrationPage("league", 2, leagueOne).hasLeagueFields(leagueOne.getName());
    }

    @Test
    public void shouldGetPageWithDivisionErrors() throws Exception {
        getAdministrationPage("division", 2, division).hasDivisionFields(division.getName());
    }

    @Test
    public void shouldGetPageWithRoundErrors() throws Exception {
        getAdministrationPage("round", 2, round).hasRoundFields(round.getDivision().getId(), round.getStartDate(), round.getEndDate());
    }

    @Test
    public void shouldGetPageWithPlayerErrors() throws Exception {
        getAdministrationPage("player", 2, playerOne).hasPlayerFields(playerOne.getUser().getId(), playerOne.getCurrentDivision().getId(), playerOne.getStatus());
    }

    @Test
    public void shouldGetPageWithMatchErrors() throws Exception {
        getAdministrationPage("match", 2, matchOne).hasMatchFields(matchOne.getRound().getId(), matchOne.getPlayerOne().getId(), matchOne.getPlayerTwo().getId(), matchOne.getScore());
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
