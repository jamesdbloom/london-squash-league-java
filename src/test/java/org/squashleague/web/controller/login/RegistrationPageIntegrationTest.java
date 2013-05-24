package org.squashleague.web.controller.login;

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
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.User;
import org.squashleague.web.configuration.WebMvcConfiguration;
import org.squashleague.web.controller.PropertyMockingApplicationContextInitializer;
import org.squashleague.web.controller.administration.MockDAOConfiguration;

import javax.annotation.Resource;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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
public class RegistrationPageIntegrationTest {

    @Resource
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Resource
    private UserDAO userDAO;

    @Before
    public void setupFixture() {
        reset(userDAO);
        mockMvc = webAppContextSetup(webApplicationContext).build();
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
        User user = new User()
                .withName("test name")
                .withEmail("test@email.com")
                .withMobile("123456789")
                .withMobilePrivacy(MobilePrivacy.SHOW_ALL)
                .withPassword("abc123$%^");

        // when
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", user.getName())
                .param("email", user.getEmail())
                .param("mobile", user.getMobile())
                .param("mobilePrivacy", user.getMobilePrivacy().name())
                .param("passwordOne", user.getPassword())
                .param("passwordTwo", user.getPassword())
        )

        // then
                .andExpect(redirectedUrl("/"));
        verify(userDAO).register(any(User.class));
    }

    @Test
    public void shouldGetPageWithPasswordErrors() throws Exception {
        // given
        User user = new User()
                .withName("test name")
                .withEmail("test@email.com")
                .withMobile("123456789")
                .withMobilePrivacy(MobilePrivacy.SHOW_ALL);

        // when
        MvcResult response = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", user.getName())
                .param("email", user.getEmail())
                .param("mobile", user.getMobile())
                .param("mobilePrivacy", user.getMobilePrivacy().name())
                .param("passwordOne", "")
                .param("passwordTwo", "ab")
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        RegistrationPage registrationPage = new RegistrationPage(response);
        registrationPage.hasErrors("user", 2);
        registrationPage.hasRegistrationFields(user.getName(), user.getEmail(), user.getMobile(), user.getMobilePrivacy());
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
                .param("passwordOne", "")
                .param("passwordTwo", "ab")
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        RegistrationPage registrationPage = new RegistrationPage(response);
        registrationPage.hasErrors("user", 6);
        registrationPage.hasRegistrationFields(user.getName(), user.getEmail(), user.getMobile(), user.getMobilePrivacy());
    }

    @Test
    public void shouldGetPageWithEmailAlreadyTakenError() throws Exception {
        // given
        User user = new User()
                .withName("duplicate name")
                .withEmail("duplicate@stupid.com")
                .withMobile("123456789")
                .withMobilePrivacy(MobilePrivacy.SHOW_ALL)
                .withPassword("abc123$%^");
        when(userDAO.findByEmail(user.getEmail())).thenReturn(user);

        MvcResult response = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", user.getName())
                .param("email", user.getEmail())
                .param("mobile", user.getMobile())
                .param("mobilePrivacy", user.getMobilePrivacy().name())
                .param("passwordOne", user.getPassword())
                .param("passwordTwo", user.getPassword())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        // then
        RegistrationPage registrationPage = new RegistrationPage(response);
        registrationPage.hasErrors("user", 1);
        registrationPage.hasRegistrationFields(user.getName(), user.getEmail(), user.getMobile(), user.getMobilePrivacy());
    }

}
