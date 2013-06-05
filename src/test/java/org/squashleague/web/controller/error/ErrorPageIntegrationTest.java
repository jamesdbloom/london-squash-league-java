package org.squashleague.web.controller.error;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.squashleague.web.controller.WebAndDataIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
