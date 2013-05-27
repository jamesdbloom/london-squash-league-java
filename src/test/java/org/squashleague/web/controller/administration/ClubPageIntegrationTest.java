package org.squashleague.web.controller.administration;

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
import org.squashleague.dao.league.ClubDAO;
import org.squashleague.domain.league.Club;
import org.squashleague.service.configuration.ServiceConfiguration;
import org.squashleague.web.configuration.WebMvcConfiguration;
import org.squashleague.web.controller.PropertyMockingApplicationContextInitializer;

import javax.annotation.Resource;

import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
                classes = RootConfiguration.class
        ),
        @ContextConfiguration(
                name = "dispatcher",
                classes = {WebMvcConfiguration.class},
                initializers = PropertyMockingApplicationContextInitializer.class
        )
})
public class ClubPageIntegrationTest {

    private final static String OBJECT_NAME = "club";
    @Resource
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Resource
    private ClubDAO clubDAO;

    // TODO copy to all other domain clubs once update completed below

    @Before
    public void setupFixture() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldSaveClubWithNoErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "test name")
                .param("address", "test address")
        )
                .andExpect(redirectedUrl("/administration"));
    }

    @Test
    public void shouldSaveClubWithErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(redirectedUrl("/administration#" + OBJECT_NAME + "s"))
                .andExpect(flash().attributeExists("bindingResult"))
                .andExpect(flash().attributeExists(OBJECT_NAME));
    }

    @Test
    public void shouldReturnPopulatedUpdateForm() throws Exception {
        Long id = 1l;
        Club club = (Club) new Club()
                .withName("test name")
                .withAddress("test address")
                .withId(id);
        club.setVersion(5);
        when(clubDAO.findById(id)).thenReturn(club);

        MvcResult response = mockMvc.perform(get("/" + OBJECT_NAME + "/update/" + id)
                .accept(MediaType.TEXT_HTML)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        ClubUpdatePage clubUpdatePage = new ClubUpdatePage(response);
        clubUpdatePage.hasClubFields(club.getId(), club.getVersion(), club.getName(), club.getAddress());
    }

    @Test
    public void shouldUpdateClubNoErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "test name")
                .param("address", "test address")
        )
                .andExpect(redirectedUrl("/administration"));
    }

    @Test
    public void shouldGetPageWithAddressError() throws Exception {
        // given
        Club club = (Club) new Club()
                .withName("test name")
                .withAddress("")
                .withId(2l);
        club.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", club.getId().toString())
                .param("version", club.getVersion().toString())
                .param("name", club.getName())
                .param("address", club.getAddress())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        ClubUpdatePage clubUpdatePage = new ClubUpdatePage(response);
        clubUpdatePage.hasErrors("club", 1);
        clubUpdatePage.hasClubFields(club.getId(), club.getVersion(), club.getName(), club.getAddress());
    }

    @Test
    public void shouldGetPageWithNameError() throws Exception {
        // given
        Club club = (Club) new Club()
                .withName("")
                .withAddress("test address")
                .withId(2l);
        club.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", club.getId().toString())
                .param("version", club.getVersion().toString())
                .param("name", club.getName())
                .param("address", club.getAddress())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        ClubUpdatePage clubUpdatePage = new ClubUpdatePage(response);
        clubUpdatePage.hasErrors("club", 1);
        clubUpdatePage.hasClubFields(club.getId(), club.getVersion(), club.getName(), club.getAddress());
    }

    @Test
    public void shouldGetPageWithAllErrors() throws Exception {
        // given
        Club club = (Club) new Club()
                .withName("test")
                .withAddress("test")
                .withId(2l);
        club.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", club.getId().toString())
                .param("version", club.getVersion().toString())
                .param("name", club.getName())
                .param("address", club.getAddress())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        ClubUpdatePage clubUpdatePage = new ClubUpdatePage(response);
        clubUpdatePage.hasErrors("club", 2);
        clubUpdatePage.hasClubFields(club.getId(), club.getVersion(), club.getName(), club.getAddress());
    }

    @Test
    public void shouldDeleteClub() throws Exception {
        // given
        Long id = 5l;

        // when
        mockMvc.perform(get("/" + OBJECT_NAME + "/delete/" + id)
                .accept(MediaType.TEXT_HTML)
        )
                // then
                .andExpect(redirectedUrl("/administration"));

        verify(clubDAO).delete(same(id));
    }

}
