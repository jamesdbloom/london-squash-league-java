package org.squashleague.service.security;

import org.squashleague.domain.account.User;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author jamesdbloom
 */
public class PasswordUpdate {

    private URL generateOneTimeTokenURL(User user, HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
        return new URL(
                "https",
                request.getLocalName(),
                request.getLocalPort(),
                "/updatePassword?email=" + URLEncoder.encode(user.getEmail(), "UTF-8") + "&oneTimeToken=" + URLEncoder.encode(user.getOneTimeToken(), "UTF-8")
        );
    }
}
