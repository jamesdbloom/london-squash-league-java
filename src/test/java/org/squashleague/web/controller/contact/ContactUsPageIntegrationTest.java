package org.squashleague.web.controller.contact;

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
import org.squashleague.domain.account.User;
import org.squashleague.service.configuration.ServiceConfiguration;
import org.squashleague.service.email.EmailMockingConfiguration;
import org.squashleague.service.email.EmailService;
import org.squashleague.service.security.SecurityMockingConfiguration;
import org.squashleague.service.security.SpringSecurityUserContext;
import org.squashleague.web.configuration.WebMvcConfiguration;
import org.squashleague.web.controller.MessagePage;

import javax.annotation.Resource;
import java.util.Arrays;

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
                classes = RootConfiguration.class),
        @ContextConfiguration(
                name = "dispatcher",
                classes = {WebMvcConfiguration.class, SecurityMockingConfiguration.class, EmailMockingConfiguration.class})
})
public class ContactUsPageIntegrationTest {

    @Resource
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Resource
    private SpringSecurityUserContext securityUserContext;
    @Resource
    private EmailService emailService;

    @Before
    public void setupFixture() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldDisplayContactUsForm() throws Exception {
        String email = "email";
        User user = mock(User.class);
        when(securityUserContext.getCurrentUser()).thenReturn(user);
        when(user.getEmail()).thenReturn(email);

        MvcResult response = mockMvc.perform(get("/contact_us").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        ContactUsPage confirmationPage = new ContactUsPage(response);
        confirmationPage.hasReadOnlyEmailField(email);
    }

    @Test
    public void shouldSendEmail() throws Exception {
        String email = "email";
        User user = mock(User.class);
        when(securityUserContext.getCurrentUser()).thenReturn(user);
        when(user.getEmail()).thenReturn(email);

        String message = "message";
        String userAgentHeader = "userAgentHeader";
        mockMvc.perform(post("/contact_us")
                .header("User-Agent", userAgentHeader)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("message", message)
        )
                .andExpect(redirectedUrl("/message"));


        String ip = "127.0.0.1";
        verify(emailService).sendContactUsMessage(message, userAgentHeader, ip, email);
    }

    @Test
    public void shouldHandleValidationErrors() throws Exception {
        String email = "email";
        User user = mock(User.class);
        when(securityUserContext.getCurrentUser()).thenReturn(user);
        when(user.getEmail()).thenReturn(email);

        byte[] randomBytes = new byte[4096];
        Arrays.fill(randomBytes, (byte) 'a');
        String userAgentHeader = "userAgentHeader";
        mockMvc.perform(post("/contact_us")
                .accept(MediaType.TEXT_HTML)
                .header("User-Agent", userAgentHeader)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("message", new String(randomBytes))
        )
                .andExpect(redirectedUrl("/message"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attributeExists("title"));

        verifyNoMoreInteractions(emailService);
    }
}
