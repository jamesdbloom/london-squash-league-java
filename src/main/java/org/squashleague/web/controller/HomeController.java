package org.squashleague.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.squashleague.service.security.SpringSecurityUserContext;

import javax.annotation.Resource;

/**
 * @author jamesdbloom
 */
@RequestMapping("/")
@Controller
public class HomeController {

    @Resource
    private SpringSecurityUserContext securityUserContext;

    @RequestMapping(method = RequestMethod.GET)
    public String getPage(Model uiModel) {
        uiModel.addAttribute("user", securityUserContext.getCurrentUser());
        return "page/home";
    }
}
