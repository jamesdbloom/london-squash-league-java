package org.squashleague.web.controller.error;

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
import org.squashleague.web.configuration.WebMvcConfiguration;
import org.squashleague.web.controller.PropertyMockingApplicationContextInitializer;
import org.squashleague.web.controller.WebAndDataIntegrationTest;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author jamesdbloom
 */
public class ErrorPageIntegrationTest extends WebAndDataIntegrationTest {

    @Test
    public void shouldReturnErrorPageForGetRequest() throws Exception {
        MvcResult response = mockMvc.perform(get("/errors/403").accept(MediaType.TEXT_HTML))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        ErrorPage errorPage = new ErrorPage(response);
        errorPage.shouldDisplayCorrectMessageAndTitle("Not Allowed", "You are not permitted to view the requested page or to perform the action you just attempted");
    }

    @Test
    public void shouldNotReturnErrorPageForPostRequest() throws Exception {
        mockMvc.perform(post("/errors/403"))
                .andExpect(status().isMethodNotAllowed());
    }
}
