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
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.User;
import org.squashleague.web.configuration.WebMvcConfiguration;
import org.squashleague.web.controller.PropertyMockingApplicationContextInitializer;
import org.squashleague.web.controller.administration.MockDAOTest;

import javax.annotation.Resource;
import javax.servlet.ServletException;

import static org.junit.Assert.assertEquals;
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
public class RegistrationPageIntegrationTest extends MockDAOTest {

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
        mockMvc.perform(get("/register").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    public void shouldRegisterUser() throws Exception {
        // given
        User expectedUser = new User()
                .withName("test name")
                .withEmail("test@email.com")
                .withMobile("123456789")
                .withMobilePrivacy(MobilePrivacy.SHOW_ALL);

        // when
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", expectedUser.getName())
                .param("email", expectedUser.getEmail())
                .param("mobile", expectedUser.getMobile())
                .param("mobilePrivacy", expectedUser.getMobilePrivacy().name())
        )

                // then
                .andExpect(redirectedUrl("/message"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("title", "Account Created"));

        User actualUser = userDAO.findByEmail(expectedUser.getEmail());

        try {
            assertEquals(expectedUser.getEmail(), actualUser.getEmail());
            assertEquals(expectedUser.getName(), actualUser.getName());
            assertEquals(expectedUser.getMobile(), actualUser.getMobile());
            assertEquals(expectedUser.getMobilePrivacy(), actualUser.getMobilePrivacy());
        } finally {
            userDAO.delete(actualUser.getId());
        }
    }

    @Test
    public void shouldGetPageWithNameError() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", users.get(0).getName())
                .param("email", users.get(0).getEmail())
                .param("mobile", users.get(0).getMobile())
                .param("mobilePrivacy", users.get(0).getMobilePrivacy().name())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        RegistrationPage registrationPage = new RegistrationPage(response);
        registrationPage.hasErrors("user", 1);
        registrationPage.hasRegistrationFields(users.get(0).getName(), users.get(0).getEmail(), users.get(0).getMobile(), users.get(0).getMobilePrivacy());
    }

    @Test
    public void shouldGetPageWithEmailError() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", users.get(0).getName())
                .param("email", users.get(0).getEmail())
                .param("mobile", users.get(0).getMobile())
                .param("mobilePrivacy", users.get(0).getMobilePrivacy().name())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        RegistrationPage registrationPage = new RegistrationPage(response);
        registrationPage.hasErrors("user", 1);
        registrationPage.hasRegistrationFields(users.get(0).getName(), users.get(0).getEmail(), users.get(0).getMobile(), users.get(0).getMobilePrivacy());
    }

    @Test
    public void shouldGetPageWithMobileError() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", users.get(0).getName())
                .param("email", users.get(0).getEmail())
                .param("mobile", users.get(0).getMobile())
                .param("mobilePrivacy", users.get(0).getMobilePrivacy().name())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        RegistrationPage registrationPage = new RegistrationPage(response);
        registrationPage.hasErrors("user", 1);
        registrationPage.hasRegistrationFields(users.get(0).getName(), users.get(0).getEmail(), users.get(0).getMobile(), users.get(0).getMobilePrivacy());
    }

    @Test
    public void shouldGetPageWithMobilePrivacyError() throws Exception {
        // given
        User user = new User()
                .withName("test name")
                .withEmail("test@email.com")
                .withMobile("123456789");

        // when
        MvcResult response = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", user.getName())
                .param("email", user.getEmail())
                .param("mobile", user.getMobile())
                .param("mobilePrivacy", "")
        )
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        RegistrationPage registrationPage = new RegistrationPage(response);
        registrationPage.hasErrors("user", 1);
        registrationPage.hasRegistrationFields(user.getName(), user.getEmail(), user.getMobile(), null);
    }

    @Test
    public void shouldGetPageWithAllErrors() throws Exception {
        // given
        User user = new User()
                .withName("ab")
                .withEmail("a@b")
                .withMobile("12345");

        // when
        MvcResult response = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", user.getName())
                .param("email", user.getEmail())
                .param("mobile", user.getMobile())
                .param("mobilePrivacy", "")
        )
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        RegistrationPage registrationPage = new RegistrationPage(response);
        registrationPage.hasErrors("user", 4);
        registrationPage.hasRegistrationFields(user.getName(), user.getEmail(), user.getMobile(), null);
    }

    @Test
    public void shouldGetPageWithEmailAlreadyTakenError() throws Exception {
        // when
        MvcResult response = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", users.get(0).getName())
                .param("email", users.get(0).getEmail())
                .param("mobile", users.get(0).getMobile())
                .param("mobilePrivacy", users.get(0).getMobilePrivacy().name())
        )
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        RegistrationPage registrationPage = new RegistrationPage(response);
        registrationPage.hasErrors("user", 1);
        registrationPage.hasRegistrationFields(users.get(0).getName(), users.get(0).getEmail(), users.get(0).getMobile(), users.get(0).getMobilePrivacy());
    }

}
