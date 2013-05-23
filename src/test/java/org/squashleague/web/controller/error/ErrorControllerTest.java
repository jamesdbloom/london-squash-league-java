package org.squashleague.web.controller.error;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.ui.Model;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author jamesdbloom
 */
public class ErrorControllerTest {

    @Test
    public void shouldDisplayForbiddenMessage() {
        // given
        Model uiModel = mock(Model.class);

        // then
        Assert.assertEquals("page/message", new ErrorController().displayForbiddenMessage(uiModel));
        verify(uiModel).addAttribute("message", "You are not permitted to view the requested page or to perform the action you just attempted");
        verify(uiModel).addAttribute("title", "Not Allowed");
    }
}
