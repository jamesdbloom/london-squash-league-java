package org.squashleague.web.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author jamesdbloom
 */
@Controller
public class ErrorController {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @RequestMapping(value = "/errors/403", method = RequestMethod.GET)
    public String displayForbiddenMessage(Model uiModel) {
        uiModel.addAttribute("message", "You are not permitted to view the requested page or to perform the action you just attempted");
        uiModel.addAttribute("title", "Not Allowed");
        return "page/message";
    }

}
