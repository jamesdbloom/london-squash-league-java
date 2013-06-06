package org.squashleague.web.controller.login;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import org.squashleague.configuration.RootConfiguration;
import org.squashleague.dao.league.HSQLApplicationContextInitializer;
import org.squashleague.domain.account.User;
import org.squashleague.service.security.SpringSecurityUserContext;
import org.squashleague.web.configuration.WebMvcConfiguration;
import org.squashleague.web.controller.PropertyMockingApplicationContextInitializer;
import org.squashleague.web.controller.administration.MockDAOTest;

import javax.annotation.Resource;
import javax.servlet.ServletException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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
                classes = RootConfiguration.class,
                initializers = HSQLApplicationContextInitializer.class
        ),
        @ContextConfiguration(
                name = "dispatcher",
                classes = WebMvcConfiguration.class,
                initializers = PropertyMockingApplicationContextInitializer.class
        )
})
public class UpdatePasswordPageIntegrationTest extends MockDAOTest {

    @Resource
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Resource
    private SpringSecurityUserContext securityUserContext;

    @Before
    public void setupFixture() throws ServletException {
        OpenEntityManagerInViewFilter openSessionInViewFilter = new OpenEntityManagerInViewFilter();
        openSessionInViewFilter.setPersistenceUnitName("persistenceUnit");
        openSessionInViewFilter.init(new MockFilterConfig(webApplicationContext.getServletContext(), "openSessionInViewFilter"));

        mockMvc = webAppContextSetup(webApplicationContext).addFilters(openSessionInViewFilter).build();
    }

    @Test
    public void shouldGetPage() throws Exception {
        mockMvc.perform(get("/register").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    public void shouldUserPassword() throws Exception {
        // when
        mockMvc.perform(post("/updatePassword")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", users.get(0).getEmail())
                .param("oneTimeToken", users.get(0).getOneTimeToken())
                .param("password", users.get(0).getPassword())
                .param("passwordConfirm", users.get(0).getPassword())
        )
                // then
                .andExpect(redirectedUrl("/account"));

        User actualUser = userDAO.findByEmail(users.get(0).getEmail());
        assertNotEquals(users.get(0).getPassword(), actualUser.getPassword());
        assertEquals("", actualUser.getOneTimeToken());

        assertEquals(users.get(0).getEmail(), securityUserContext.getCurrentUser().getEmail());
    }

    @Test
    public void shouldGetPageWithPasswordFormatError() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/updatePassword")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", users.get(0).getEmail())
                .param("oneTimeToken", users.get(0).getOneTimeToken())
                .param("password", "invalid_format")
                .param("passwordConfirm", users.get(0).getPassword())
        )
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        UpdatePasswordPage registrationPage = new UpdatePasswordPage(response);
        registrationPage.hasErrors("password", 2);
    }

    @Test
    public void shouldGetPageWithPasswordsMatchError() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/updatePassword")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", users.get(0).getEmail())
                .param("oneTimeToken", users.get(0).getOneTimeToken())
                .param("password", users.get(0).getPassword())
                .param("passwordConfirm", "none_matching_password")
        )
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        UpdatePasswordPage registrationPage = new UpdatePasswordPage(response);
        registrationPage.hasErrors("password", 1);
    }

}
