package org.squashleague.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author jamesdbloom
 */
@RequestMapping("/webping")
@Controller
public class WebPingController {

    /*
    if [ "`curl -k -s -L http://localhost/webping`" != "OK" ]; then echo "down"; fi
     */

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody String getPage() {
        return "OK";
    }
}
