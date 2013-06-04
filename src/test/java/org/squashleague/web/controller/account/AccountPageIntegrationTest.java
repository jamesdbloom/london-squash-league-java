package org.squashleague.web.controller.account;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.squashleague.web.controller.WebAndDataIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author jamesdbloom
 */
public class AccountPageIntegrationTest extends WebAndDataIntegrationTest {

    @Test
    public void shouldDisplayUserDetails() throws Exception {
        securityUserContext.setCurrentUser(userOne);

        try {
            MvcResult result = mockMvc.perform(get("/account").accept(MediaType.TEXT_HTML))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andReturn();

            AccountPage accountPage = new AccountPage(result);
            accountPage.hasUserDetails(userOne);
        } finally {
            securityUserContext.setCurrentUser(LOGGED_IN_USER);
        }
    }
}
