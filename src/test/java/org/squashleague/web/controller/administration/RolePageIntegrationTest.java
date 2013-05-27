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
import org.squashleague.dao.account.RoleDAO;
import org.squashleague.domain.account.Role;
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
public class RolePageIntegrationTest {

    private final static String OBJECT_NAME = "role";
    @Resource
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Resource
    private RoleDAO roleDAO;

    @Before
    public void setupFixture() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldSaveRoleWithNoErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "test name")
                .param("description", "test description")
        )
                .andExpect(redirectedUrl("/administration"));
    }

    @Test
    public void shouldSaveRoleWithErrors() throws Exception {
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
        Role role = (Role) new Role()
                .withName("test name")
                .withDescription("test description")
                .withId(id);
        role.setVersion(5);
        when(roleDAO.findById(id)).thenReturn(role);

        MvcResult response = mockMvc.perform(get("/" + OBJECT_NAME + "/update/" + id)
                .accept(MediaType.TEXT_HTML)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        RoleUpdatePage roleUpdatePage = new RoleUpdatePage(response);
        roleUpdatePage.hasRoleFields(role.getId(), role.getVersion(), role.getName(), role.getDescription());
    }

    @Test
    public void shouldUpdateRoleNoErrors() throws Exception {
        mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "test name")
                .param("description", "test description")
        )
                .andExpect(redirectedUrl("/administration"));
    }

    @Test
    public void shouldGetPageWithDescriptionError() throws Exception {
        // given
        Role role = (Role) new Role()
                .withName("test name")
                .withDescription("")
                .withId(2l);
        role.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", role.getId().toString())
                .param("version", role.getVersion().toString())
                .param("name", role.getName())
                .param("description", role.getDescription())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        RoleUpdatePage roleUpdatePage = new RoleUpdatePage(response);
        roleUpdatePage.hasErrors("role", 1);
        roleUpdatePage.hasRoleFields(role.getId(), role.getVersion(), role.getName(), role.getDescription());
    }

    @Test
    public void shouldGetPageWithNameError() throws Exception {
        // given
        Role role = (Role) new Role()
                .withName("")
                .withDescription("test description")
                .withId(2l);
        role.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", role.getId().toString())
                .param("version", role.getVersion().toString())
                .param("name", role.getName())
                .param("description", role.getDescription())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        RoleUpdatePage roleUpdatePage = new RoleUpdatePage(response);
        roleUpdatePage.hasErrors("role", 1);
        roleUpdatePage.hasRoleFields(role.getId(), role.getVersion(), role.getName(), role.getDescription());
    }

    @Test
    public void shouldGetPageWithAllErrors() throws Exception {
        // given
        Role role = (Role) new Role()
                .withName("test")
                .withDescription("test")
                .withId(2l);
        role.setVersion(5);

        // when
        MvcResult response = mockMvc.perform(post("/" + OBJECT_NAME + "/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", role.getId().toString())
                .param("version", role.getVersion().toString())
                .param("name", role.getName())
                .param("description", role.getDescription())
        )

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andReturn();

        RoleUpdatePage roleUpdatePage = new RoleUpdatePage(response);
        roleUpdatePage.hasErrors("role", 2);
        roleUpdatePage.hasRoleFields(role.getId(), role.getVersion(), role.getName(), role.getDescription());
    }

    @Test
    public void shouldDeleteRole() throws Exception {
        // given
        Long id = 5l;

        // when
        mockMvc.perform(get("/" + OBJECT_NAME + "/delete/" + id)
                .accept(MediaType.TEXT_HTML)
        )
                // then
                .andExpect(redirectedUrl("/administration"));

        verify(roleDAO).delete(same(id));
    }

}
