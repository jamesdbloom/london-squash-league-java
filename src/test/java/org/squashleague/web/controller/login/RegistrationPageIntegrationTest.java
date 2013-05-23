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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.WebApplicationContext;
import org.squashleague.configuration.RootConfiguration;
import org.squashleague.dao.account.RoleDAO;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;
import org.squashleague.web.configuration.WebMvcConfiguration;
import org.squashleague.web.controller.PropertyMockingApplicationContextInitializer;
import org.squashleague.web.controller.administration.MockDAOConfiguration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
public class RegistrationPageIntegrationTest {

    @Resource
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setupFixture() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldGetPage() throws Exception {
        mockMvc.perform(get("/register").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    public void shouldGetPageWithPasswordErrors() throws Exception {
        // given
        User object = new User()
                .withName("test name")
                .withEmail("test@email.com")
                .withMobile("123456789")
                .withMobilePrivacy(MobilePrivacy.SHOW_ALL);

        // when
        MvcResult response = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", object.getName())
                .param("email", object.getEmail())
                .param("mobile", object.getMobile())
                .param("mobilePrivacy", object.getMobilePrivacy().name())
                .param("passwordOne", "")
                .param("passwordTwo", "ab")
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        RegistrationPage registrationPage = new RegistrationPage(response);
        registrationPage.hasErrors("user", 2);
        registrationPage.hasRegistrationFields(object.getName(), object.getEmail(), object.getMobile(), object.getMobilePrivacy());
    }

    @Test
    public void shouldGetPageWithAllErrors() throws Exception {
        // given
        User object = new User()
                .withName("ab")
                .withEmail("a@b")
                .withMobile("12345");

        // when
        MvcResult response = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", object.getName())
                .param("email", object.getEmail())
                .param("mobile", object.getMobile())
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
        registrationPage.hasRegistrationFields(object.getName(), object.getEmail(), object.getMobile(), object.getMobilePrivacy());
    }

}
