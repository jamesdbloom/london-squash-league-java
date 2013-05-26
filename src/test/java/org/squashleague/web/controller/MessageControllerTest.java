package org.squashleague.web.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.squashleague.web.controller.MessageController;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;

/**
 * @author jamesdbloom
 */
@RunWith(MockitoJUnitRunner.class)
public class MessageControllerTest {

    @Test
    public void shouldReturnCorrectConfirmationPage() {
        assertEquals("page/message", new MessageController().messagePage());
    }

}
