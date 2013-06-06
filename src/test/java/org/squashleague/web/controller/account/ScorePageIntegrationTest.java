package org.squashleague.web.controller.account;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.squashleague.domain.league.Match;
import org.squashleague.web.controller.WebAndDataIntegrationTest;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author jamesdbloom
 */
public class ScorePageIntegrationTest extends WebAndDataIntegrationTest {

    @Test
    public void shouldGetPage() throws Exception {
        MvcResult result = mockMvc.perform(get("/score/" + matches.get(0).getId()).accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        ScorePage scorePage = new ScorePage(result);
        scorePage.hasMessage(matches.get(0));
    }

    @Test
    public void shouldUpdateScore() throws Exception {
        // when
        String score = "3-2";
        String referer = "/foo";
        mockMvc.perform(post("/score")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("referer", referer)
                .param("id", matches.get(0).getId().toString())
                .param("score", score)
        )
                // then
                .andExpect(redirectedUrl(referer));

        Match actualMatch = matchDAO.findById(matches.get(0).getId());
        assertEquals(score, actualMatch.getScore());
        assertEquals(new DateTime().toString("dd MMM yyyy"), actualMatch.getScoreEntered().toString("dd MMM yyyy"));
    }
}
