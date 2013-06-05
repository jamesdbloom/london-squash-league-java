package org.squashleague.web.controller;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author jamesdbloom
 */
public class MessagePageIntegrationTest extends WebAndDataIntegrationTest {

    @Test
    public void shouldDisplayMessage() throws Exception {
        String message = "test message";
        String title = "test title";

        MvcResult response = mockMvc.perform(get("/message").accept(MediaType.TEXT_HTML)
                .flashAttr("message", message)
                .flashAttr("title", title)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        MessagePage confirmationPage = new MessagePage(response);
        confirmationPage.hasMessage(message);
        confirmationPage.hasTitle(title);
    }

    @Test
    public void shouldDisplayErrorMessage() throws Exception {
        String message = "test message";
        String title = "test title";

        MvcResult response = mockMvc.perform(get("/message").accept(MediaType.TEXT_HTML)
                .flashAttr("message", message)
                .flashAttr("title", title)
                .flashAttr("error", true)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        MessagePage confirmationPage = new MessagePage(response);
        confirmationPage.hasErrorMessage(message);
        confirmationPage.hasTitle(title);
    }
}
