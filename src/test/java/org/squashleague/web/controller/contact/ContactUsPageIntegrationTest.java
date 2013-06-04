package org.squashleague.web.controller.contact;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.squashleague.service.email.EmailService;
import org.squashleague.web.controller.WebAndDataIntegrationTest;

import javax.annotation.Resource;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author jamesdbloom
 */
public class ContactUsPageIntegrationTest extends WebAndDataIntegrationTest {

    @Resource
    private EmailService emailService;

    private static final String IP = "127.0.0.1";

    @Test
    public void shouldDisplayContactUsForm() throws Exception {
        MvcResult response = mockMvc.perform(get("/contact_us").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        ContactUsPage confirmationPage = new ContactUsPage(response);
        confirmationPage.hasReadOnlyEmailField(LOGGED_IN_USER_EMAIL);
    }

    @Test
    public void shouldSendEmail() throws Exception {
        String message = "message";
        String userAgentHeader = "userAgentHeader";
        mockMvc.perform(post("/contact_us")
                .header("User-Agent", userAgentHeader)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("message", message)
        )
                .andExpect(redirectedUrl("/message"));


        verify(emailService).sendContactUsMessage(message, userAgentHeader, IP, LOGGED_IN_USER_EMAIL);
    }

    @Test
    public void shouldHandleValidationErrors() throws Exception {
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
