package org.squashleague.web.controller;

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
import org.squashleague.dao.league.HSQLApplicationContextInitializer;
import org.squashleague.service.security.SecurityMockingConfiguration;
import org.squashleague.web.configuration.WebMvcConfiguration;

import javax.annotation.Resource;

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
                classes = {WebMvcConfiguration.class},
                initializers = PropertyMockingApplicationContextInitializer.class
        )
})
public class MessagePageIntegrationTest {

    @Resource
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setupFixture() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

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
