package org.squashleague.service.http;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jamesdbloom
 */
@Component
public class RequestParser {

    private static final String[] IP_FORWARDING_HEADERS = {"X-Ip", "X-Forwarded-For"};
    private static final java.util.regex.Pattern IPv4_PATTERN = java.util.regex.Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$");
    private static final java.util.regex.Pattern IPv6_PATTERN = java.util.regex.Pattern.compile("^([\\dA-Fa-f]{1,4}:|((?=.*(::))(?!.*\\3.+\\3))\\3?)([\\dA-Fa-f]{1,4}(\\3|:\\b)|\\2){5}(([\\dA-Fa-f]{1,4}(\\3|:\\b|$)|\\2){2}|(((2[0-4]|1\\d|[1-9])?\\d|25[0-5])\\.?\\b){4})$");

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
}
