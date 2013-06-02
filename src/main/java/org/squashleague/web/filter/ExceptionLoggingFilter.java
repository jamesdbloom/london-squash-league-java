package org.squashleague.web.filter;

import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jamesdbloom
 */
public class ExceptionLoggingFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            logger.warn(getExceptionMessage(e, request), e);
            if (response instanceof HttpServletResponse) {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }

    private String getExceptionMessage(final Exception e, final ServletRequest request) {
        final String path;
        if (request instanceof HttpServletRequest) {
            path = ((HttpServletRequest) request).getRequestURI();
        } else {
            path = "";
        }

        return String.format("Caught %s handling path %s", e.getClass().getSimpleName(), path);
    }

    @Override
    public void destroy() {
    }
}
