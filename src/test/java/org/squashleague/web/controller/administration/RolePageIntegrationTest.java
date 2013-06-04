package org.squashleague.web.controller.administration;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.squashleague.domain.account.Role;
import org.squashleague.web.controller.WebAndDataIntegrationTest;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author jamesdbloom
 */
public class RolePageIntegrationTest extends WebAndDataIntegrationTest {

    private final static String OBJECT_NAME = "role";

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
        MvcResult response = mockMvc.perform(get("/" + OBJECT_NAME + "/update/" + role.getId())
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
        Role role = new Role()
                .withName("new role")
                .withDescription("new description");
        roleDAO.save(role);

        // when
        mockMvc.perform(get("/" + OBJECT_NAME + "/delete/" + role.getId())
                .accept(MediaType.TEXT_HTML)
        )
                // then
                .andExpect(redirectedUrl("/administration"));

        assertNull(roleDAO.findById(role.getId()));
    }

}
