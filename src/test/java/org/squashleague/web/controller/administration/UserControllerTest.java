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
import org.squashleague.dao.account.UserDAO;
import org.squashleague.domain.account.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author jamesdbloom
 */
@RunWith(VerboseMockitoJUnitRunner.class)
public class UserControllerTest {

    private final List<User> users = new ArrayList<>();
    @Mock
    private UserDAO userDAO;
    @InjectMocks
    private UserController userController = new UserController();

    @Before
    public void setupFixture() {
        when(userDAO.findAll()).thenReturn(users);
    }

    @Test
    public void shouldSaveUserAndRedirectWhenNoBindingErrors() throws Exception {
        // given
        User user = new User();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // when
        String page = userController.create(user, mock(BindingResult.class), redirectAttributes);

        // then
        verify(userDAO).save(same(user));
        assertEquals("redirect:/administration", page);
    }

    @Test
    public void shouldAddBindingErrorsToSessionAndRedirect() throws Exception {
        // given
        User user = new User();
        String objectName = "user";
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // when
        String page = userController.create(user, bindingResult, redirectAttributes);

        // then
        verify(redirectAttributes).addFlashAttribute(eq("bindingResult"), same(bindingResult));
        verify(redirectAttributes).addFlashAttribute(eq(objectName), same(user));
        assertEquals("redirect:/administration#" + objectName + "s", page);
    }

    @Test
    public void shouldRetrieveCorrectUserAndDisplayForm() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        Long id = 1l;
        User user = new User();
        when(userDAO.findById(same(id))).thenReturn(user);

        // when
        String page = userController.updateForm(id, uiModel);

        // then
        verify(uiModel).addAttribute(eq("user"), same(user));
        assertEquals("page/user/update", page);
    }

    @Test
    public void shouldUpdateUserAndRedirectWhenNoBindingErrors() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        User user = new User();
        BindingResult bindingResult = mock(BindingResult.class);

        // when
        String page = userController.update(user, bindingResult, uiModel);

        // then
        verify(userDAO).update(same(user));
        assertEquals("redirect:/account", page);
    }

    @Test
    public void shouldAddBindingErrorsToModelAndForward() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        User user = new User();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        // when
        String page = userController.update(user, bindingResult, uiModel);

        // then
        verify(uiModel).addAttribute(eq("user"), same(user));
        assertEquals("page/user/update", page);
    }

    @Test
    public void shouldDeleteAndRedirect() throws Exception {
        // given
        Long id = 1l;

        // when
        String page = userController.delete(id);

        // then
        verify(userDAO).delete(same(id));
        assertEquals("redirect:/administration", page);
    }

}
