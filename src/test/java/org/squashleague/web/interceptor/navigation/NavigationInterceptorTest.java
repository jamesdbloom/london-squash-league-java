package org.squashleague.web.interceptor.navigation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

/**
 * @author jamesdbloom
 */
@RunWith(MockitoJUnitRunner.class)
public class NavigationInterceptorTest {

    @Test
    public void shouldAddPageRedirect() throws Exception {
        // given - setupFixture and
        ModelAndView modelAndView = new ModelAndView("a");

        // when
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setRequestURI("b/c");
        new NavigationInterceptor().postHandle(mockHttpServletRequest, null, null, modelAndView);

        // then
        assertEquals(modelAndView.getModel().get("page"), "c");
    }

    @Test
    public void shouldNotAddPageAttributeForRedirect() throws Exception {
        // given - setupFixture and
        ModelAndView modelAndView = new ModelAndView("redirect:a");

        // when
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setRequestURI("b/c");
        new NavigationInterceptor().postHandle(mockHttpServletRequest, null, null, modelAndView);

        // then
        assertNull(modelAndView.getModel().get("page"));
    }
}
