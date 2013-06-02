package org.squashleague.web.filter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

/**
 * @author jamesdbloom
 */
@RunWith(MockitoJUnitRunner.class)
public class ExceptionLoggingFilterTest {

    @Mock
    private FilterChain mockChain;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private Logger logger;
    @InjectMocks
    private ExceptionLoggingFilter filter = new ExceptionLoggingFilter();

    @Test
    public void shouldDoNothingIfNoException() throws Exception {
        filter.doFilter(mockRequest, mockResponse, mockChain);

        verify(mockChain).doFilter(mockRequest, mockResponse);
    }

    @Test
    public void shouldSendErrorOnException() throws Exception {
        RuntimeException runtimeException = new RuntimeException();
        doThrow(runtimeException).when(mockChain).doFilter(mockRequest, mockResponse);
        when(mockRequest.getRequestURI()).thenReturn("path");

        filter.doFilter(mockRequest, mockResponse, mockChain);

        verify(logger).warn("Caught RuntimeException handling path path", runtimeException);
        verify(mockChain).doFilter(mockRequest, mockResponse);
        verify(mockResponse).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}
