package org.squashleague.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author jamesdbloom
 */
@RequestMapping("/")
@Controller
public class HomeController {

    @RequestMapping(method = RequestMethod.GET)
    public String getPage() {
        return "page/home";
    }
}
