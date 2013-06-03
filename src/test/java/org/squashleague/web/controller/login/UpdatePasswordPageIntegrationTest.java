package org.squashleague.web.controller.login;

import com.eaio.uuid.UUID;
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
import org.squashleague.dao.league.HSQLApplicationContextInitializer;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.User;
import org.squashleague.service.security.SecurityMockingConfiguration;
import org.squashleague.service.security.SpringSecurityUserContext;
import org.squashleague.web.configuration.WebMvcConfiguration;
import org.squashleague.web.controller.PropertyMockingApplicationContextInitializer;
import org.squashleague.web.controller.administration.MockDAOTest;

import javax.annotation.Resource;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
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
                classes = {WebMvcConfiguration.class},
                initializers = PropertyMockingApplicationContextInitializer.class
        )
})
public class UpdatePasswordPageIntegrationTest extends MockDAOTest {

    @Resource
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Resource
    private UserDAO userDAO;
    @Resource
    private SpringSecurityUserContext securityUserContext;

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
                .withPassword("abc123$%^")
                .withOneTimeToken(new UUID().toString());
        when(userDAO.findByEmail(user.getEmail())).thenReturn(user);

        // when
        mockMvc.perform(post("/updatePassword")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", user.getEmail())
                .param("oneTimeToken", user.getOneTimeToken())
                .param("password", user.getPassword())
                .param("passwordConfirm", user.getPassword())
        )
                // then
                .andExpect(redirectedUrl("/account"));

        verify(userDAO).updatePassword(any(User.class));
        verify(securityUserContext).setCurrentUser(any(User.class));
    }

    @Test
    public void shouldGetPageWithPasswordFormatError() throws Exception {
        // given
        User user = new User()
                .withName("test name")
                .withEmail("test@email.com")
                .withMobile("123456789")
                .withMobilePrivacy(MobilePrivacy.SHOW_ALL)
                .withPassword("invalid_format")
                .withOneTimeToken(new UUID().toString());
        when(userDAO.findByEmail(user.getEmail())).thenReturn(user);

        // when
        MvcResult response = mockMvc.perform(post("/updatePassword")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", user.getEmail())
                .param("oneTimeToken", user.getOneTimeToken())
                .param("password", user.getPassword())
                .param("passwordConfirm", user.getPassword())
        )
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        UpdatePasswordPage registrationPage = new UpdatePasswordPage(response);
        registrationPage.hasErrors("password", 1);
        verify(userDAO, times(0)).update(any(User.class));
        verifyZeroInteractions(securityUserContext);
    }

    @Test
    public void shouldGetPageWithPasswordsMatchError() throws Exception {
        // given
        User user = new User()
                .withName("test name")
                .withEmail("test@email.com")
                .withMobile("123456789")
                .withMobilePrivacy(MobilePrivacy.SHOW_ALL)
                .withPassword("abc123$%^")
                .withOneTimeToken(new UUID().toString());
        when(userDAO.findByEmail(user.getEmail())).thenReturn(user);

        // when
        MvcResult response = mockMvc.perform(post("/updatePassword")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", user.getEmail())
                .param("oneTimeToken", user.getOneTimeToken())
                .param("password", user.getPassword())
                .param("passwordConfirm", "none_matching_password")
        )
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        UpdatePasswordPage registrationPage = new UpdatePasswordPage(response);
        registrationPage.hasErrors("password", 1);
        verify(userDAO, times(0)).update(any(User.class));
        verifyZeroInteractions(securityUserContext);
    }

}
