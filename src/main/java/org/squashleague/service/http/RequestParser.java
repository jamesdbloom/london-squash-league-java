package org.squashleague.service.http;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jamesdbloom
 */
@Component
public class RequestParser {

    private static final String[] IP_FORWARDING_HEADERS = {"X-Ip", "X-Forwarded-For"};
    private static final Pattern IPv4_PATTERN = Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$");
    private static final Pattern IPv6_PATTERN = Pattern.compile("^([\\dA-Fa-f]{1,4}:|((?=.*(::))(?!.*\\3.+\\3))\\3?)([\\dA-Fa-f]{1,4}(\\3|:\\b)|\\2){5}(([\\dA-Fa-f]{1,4}(\\3|:\\b|$)|\\2){2}|(((2[0-4]|1\\d|[1-9])?\\d|25[0-5])\\.?\\b){4})$");
    private static final Pattern URL_PATTERN = Pattern.compile("((https?:)?\\/\\/)([\\da-zA-Z\\.-]+)\\.([a-zA-Z\\.]{2,6})(\\:[0-9]*)?([\\/\\w \\.-]*)\\/?");
    private static final Pattern URI_PATH_PATTERN = Pattern.compile("([\\/\\w \\.-]*)\\/?");

    public String getIpAddress(HttpServletRequest request) {

        String ipInHeader = null;
        //check for presence of forwarding headers
        for (String header : IP_FORWARDING_HEADERS) {
            String ip = request.getHeader(header.trim());
            if (ip != null) {
                //so it exists but we need to make sure that it is not a comma separated list
                String[] tokens = ip.split(",");
                ipInHeader = tokens[0].trim();
                break;
            }
        }
        //if we didn't find one then get the remote IP
        if (ipInHeader == null) {
            ipInHeader = request.getRemoteAddr();
        }

        if (IPv4_PATTERN.matcher(ipInHeader).find() || IPv6_PATTERN.matcher(ipInHeader).find()) {
            return ipInHeader;
        }

        return "";
    }

    public String parseRelativeURI(String referer, String defaultValue) {
        Matcher matcher = URL_PATTERN.matcher(referer);
        if (matcher.matches()) {
            return matcher.group(6) + (matcher.group(6).isEmpty() ? "/" : "");
        } else if (URI_PATH_PATTERN.matcher(referer).matches()) {
            return referer;
        }
        return defaultValue;
    }
}
