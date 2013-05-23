package org.squashleague.web.controller.administration;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.squashleague.dao.account.RoleDAO;
import org.squashleague.dao.account.UserDAO;
import org.squashleague.domain.account.MobilePrivacy;
import org.squashleague.domain.account.Role;
import org.squashleague.domain.account.User;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author jamesdbloom
 */
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private final List<User> users = new ArrayList<>();
    @Mock
    private UserDAO userDAO;
    @Mock
    private RoleDAO roleDAO;
    @Resource
    private Environment environment;
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
        List<Role> roles = Lists.newArrayList(
                new Role()
                        .withName("role one name")
                        .withDescription("role one description"),
                new Role()
                        .withName("role two name")
                        .withDescription("role two description")
        );
        when(roleDAO.findAll()).thenReturn(roles);

        // when
        String page = userController.updateForm(id, uiModel);

        // then
        verify(uiModel).addAttribute(eq("user"), same(user));
        verify(uiModel).addAttribute(eq("roles"), same(roles));
        verify(uiModel).addAttribute(eq("environment"), same(environment));
        verify(uiModel).addAttribute("mobilePrivacyOptions", MobilePrivacy.enumToFormOptionMap());
        verify(uiModel).addAttribute(eq("emailPattern"), same(User.EMAIL_PATTERN));
        assertEquals("page/user/update", page);
    }

    @Test
    public void shouldUpdateUserAndRedirectWhenNoBindingErrors() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        User user = (User) new User().withId(5l);
        User existingUser = new User().withPassword("password");
        when(userDAO.findById(same(user.getId()))).thenReturn(existingUser);

        // when
        String page = userController.update(user, mock(BindingResult.class), uiModel);

        // then
        verify(userDAO).update(same(user));
        assertEquals(user.getPassword(), existingUser.getPassword());
        assertEquals("redirect:/account", page);
    }

    @Test
    public void shouldAddBindingErrorsToModelAndForward() throws Exception {
        // given
        Model uiModel = mock(Model.class);
        User user = new User();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        List<Role> roles = Lists.newArrayList(
                new Role()
                        .withName("role one name")
                        .withDescription("role one description"),
                new Role()
                        .withName("role two name")
                        .withDescription("role two description")
        );
        when(roleDAO.findAll()).thenReturn(roles);

        // when
        String page = userController.update(user, bindingResult, uiModel);

        // then
        verify(uiModel).addAttribute(eq("user"), same(user));
        verify(uiModel).addAttribute(eq("roles"), same(roles));
        verify(uiModel).addAttribute(eq("environment"), same(environment));
        verify(uiModel).addAttribute("mobilePrivacyOptions", MobilePrivacy.enumToFormOptionMap());
        verify(uiModel).addAttribute(eq("emailPattern"), same(User.EMAIL_PATTERN));
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
