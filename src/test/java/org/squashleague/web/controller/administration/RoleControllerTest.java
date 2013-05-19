package org.squashleague.web.controller.administration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.VerboseMockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.account.RoleDAO;
import org.squashleague.domain.account.Role;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author jamesdbloom
 */
@RunWith(VerboseMockitoJUnitRunner.class)
public class RoleControllerTest {

    private final List<Role> roles = new ArrayList<>();
    @Mock
    private RoleDAO roleDAO;
    @InjectMocks
    private RoleController roleController = new RoleController();

    @Before
    public void setupFixture() {
        when(roleDAO.findAll()).thenReturn(roles);
    }

    @Test
    public void shouldSaveRoleAndRedirectWhenNoBindingErrors() throws Exception {
        // given
        Role role = new Role();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // when
        String page = roleController.create(role, mock(BindingResult.class), redirectAttributes);

        // then
        verify(roleDAO).save(same(role));
        assertEquals("redirect:/administration", page);
    }

    @Test
    public void shouldAddBindingErrorsToSessionAndRedirect() throws Exception {
        // given
        Role role = new Role();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // when
        String page = roleController.create(role, bindingResult, redirectAttributes);

        // then
        verify(redirectAttributes).addFlashAttribute(eq("bindingResult"), same(bindingResult));
        verify(redirectAttributes).addFlashAttribute(eq("role"), same(role));
        assertEquals("redirect:/administration", page);
    }

    @Test
    public void shouldRetrieveCorrectRoleAndDisplayForm() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Long id = 1l;
        Role role = new Role();
        when(roleDAO.findOne(same(id))).thenReturn(role);

        // when
        String page = roleController.updateForm(id, uiModel);

        // then
        verify(uiModel).addAttribute(eq("role"), same(role));
        assertEquals("page/role/update", page);
    }

    @Test
    public void shouldUpdateRoleAndRedirectWhenNoBindingErrors() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Role role = new Role();
        BindingResult bindingResult = mock(BindingResult.class);

        // when
        String page = roleController.update(role, bindingResult, uiModel);

        // then
        verify(roleDAO).update(same(role));
        assertEquals("redirect:/account", page);
    }

    @Test
    public void shouldAddBindingErrorsToModelAndForward() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Role role = new Role();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // when
        String page = roleController.update(role, bindingResult, uiModel);

        // then
        verify(uiModel).addAttribute(eq("role"), same(role));
        assertEquals("page/role/update", page);
    }

    @Test
    public void shouldDeleteAndRedirect() throws Exception {
        // given
        Long id = 1l;

        // when
        String page = roleController.delete(id);

        // then
        verify(roleDAO).delete(same(id));
        assertEquals("redirect:/administration", page);
    }

}
