package org.squashleague.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author squashleague
 */
@Controller
public class LoginPageController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getPage() {
        return "page/login";
    }
}
