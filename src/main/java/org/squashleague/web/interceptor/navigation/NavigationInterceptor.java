package org.squashleague.web.interceptor.navigation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jamesdbloom
 */
public class NavigationInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && !modelAndView.getViewName().startsWith("redirect:")) {
            modelAndView.addObject("page", StringUtils.substringAfterLast(request.getRequestURI(), "/"));
        }
    }

}
