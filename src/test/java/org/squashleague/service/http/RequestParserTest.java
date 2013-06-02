package org.squashleague.service.http;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.Assert.assertEquals;

/**
 * @author jamesdbloom
 */
public class RequestParserTest {

    private MockHttpServletRequest mockHttpServletRequest;
    private RequestParser requestParser = new RequestParser();

    @Before
    public void setup() {
        mockHttpServletRequest = new MockHttpServletRequest();
    }

    @Test
    public void shouldGetIPWhenXForwardedForPresent() {
        //given
        mockHttpServletRequest.addHeader("X-Forwarded-For", "1.2.3.4");
        mockHttpServletRequest.setRemoteAddr("9.9.9.9");
        //then
        assertEquals("1.2.3.4", requestParser.getIpAddress(mockHttpServletRequest));
    }

    @Test
    public void shouldGetIPWhenMultipleXForwardedForPresent() {
        //given
        mockHttpServletRequest.addHeader("X-Forwarded-For", "1.2.3.4, 1.1.1.1, 2.2.2.2");
        //then
        assertEquals("1.2.3.4", requestParser.getIpAddress(mockHttpServletRequest));
    }

    @Test
    public void shouldGetIPWhenXIpPresent() {
        //given
        mockHttpServletRequest.addHeader("X-Ip", "1.2.3.4");
        mockHttpServletRequest.setRemoteAddr("9.9.9.9");
        //then
        assertEquals("1.2.3.4", requestParser.getIpAddress(mockHttpServletRequest));
    }

    @Test
    public void shouldGetIPWhenMultipleXIpPresent() {
        //given
        mockHttpServletRequest.addHeader("X-Ip", "86.141.115.191, 84.20.192.254 ");
        //then
        assertEquals("86.141.115.191", requestParser.getIpAddress(mockHttpServletRequest));
    }

    @Test
    public void shouldGetXIpPreferentiallyWhenMultipleXIpPresent() {
        //given
        mockHttpServletRequest.addHeader("X-Ip", "1.2.3.4, 1.1.1.1, 2.2.2.2");
        mockHttpServletRequest.addHeader("X-Forwarded-For", "3.3.3.3, 1.1.1.1, 2.2.2.2");
        mockHttpServletRequest.setRemoteAddr("9.9.9.9");
        //then
        assertEquals("1.2.3.4", requestParser.getIpAddress(mockHttpServletRequest));
    }

    @Test
    public void shouldGetRemoteAddressWhenNoXForwardingHeaders() {
        //given
        mockHttpServletRequest.setRemoteAddr("9.9.9.9");
        //then
        assertEquals("9.9.9.9", requestParser.getIpAddress(mockHttpServletRequest));
    }

    @Test
    public void shouldHandleIPv6() {
        //given
        mockHttpServletRequest.setRemoteAddr("2001:0db8:85a3:0042:1000:8a2e:0370:7334");
        //then
        assertEquals("2001:0db8:85a3:0042:1000:8a2e:0370:7334", requestParser.getIpAddress(mockHttpServletRequest));
    }

    @Test
    public void shouldHandleIPv6Loopback() {
        //given
        mockHttpServletRequest.setRemoteAddr("0:0:0:0:0:0:0:1");
        //then
        assertEquals("0:0:0:0:0:0:0:1", requestParser.getIpAddress(mockHttpServletRequest));

        //given
        mockHttpServletRequest.setRemoteAddr("0000:0000:0000:0000:0000:0000:0000:0001");
        //then
        assertEquals("0000:0000:0000:0000:0000:0000:0000:0001", requestParser.getIpAddress(mockHttpServletRequest));

        //given
        mockHttpServletRequest.setRemoteAddr("::1");
        //then
        assertEquals("::1", requestParser.getIpAddress(mockHttpServletRequest));
    }

    @Test
    public void shouldHandleIPv4AsIPv6() {
        //given
        mockHttpServletRequest.setRemoteAddr("::192.0.2.128");
        //then
        assertEquals("::192.0.2.128", requestParser.getIpAddress(mockHttpServletRequest));
    }

    @Test
    public void shouldNotReturnInvalidIpAddressesThatContainLetters() {
        //given
        mockHttpServletRequest.setRemoteAddr("this.is.not.a.valid.address");
        //then
        assertEquals("", requestParser.getIpAddress(mockHttpServletRequest));
    }

    @Test
    public void shouldNotReturnInvalidIpAddressesThatAreTooLong() {
        //given
        mockHttpServletRequest.setRemoteAddr("1234.1234.1234.1234");
        //then
        assertEquals("", requestParser.getIpAddress(mockHttpServletRequest));
    }

    @Test
    public void shouldHandleFullURL() {
        assertEquals("/foo/bar", requestParser.parseRelativeURI("http://www.google.com/foo/bar", "default"));
    }

    @Test
    public void shouldHandleEmptyPathWithSlash() {
        assertEquals("/", requestParser.parseRelativeURI("http://www.google.com", "default"));
    }

    @Test
    public void shouldHandleEmptyPath() {
        assertEquals("/", requestParser.parseRelativeURI("http://www.google.com", "default"));
    }

    @Test
    public void shouldHandleFullURLWithPort() {
        assertEquals("/foo/bar", requestParser.parseRelativeURI("http://www.google.com:8080/foo/bar", "default"));
    }

    @Test
    public void shouldHandleFullURLWithHTTPS() {
        assertEquals("/foo/bar", requestParser.parseRelativeURI("https://www.google.com/foo/bar", "default"));
    }

    @Test
    public void shouldHandleFullURLWithCapitals() {
        assertEquals("/foo/bar", requestParser.parseRelativeURI("http://www.GOOGLE.com/foo/bar", "default"));
    }

    @Test
    public void shouldHandleFullURLWithCoUk() {
        assertEquals("/foo/bar", requestParser.parseRelativeURI("http://www.google.co.uk/foo/bar", "default"));
    }

    @Test
    public void shouldHandleFullURLWithDoubleSlash() {
        assertEquals("/foo/bar", requestParser.parseRelativeURI("//www.GOOGLE.co.uk/foo/bar", "default"));
    }

    @Test
    public void shouldHandleRelativePathWithMiddleSlash() {
        assertEquals("foo/bar", requestParser.parseRelativeURI("foo/bar", "default"));
    }

    @Test
    public void shouldHandleRelativePath() {
        assertEquals("foo", requestParser.parseRelativeURI("foo", "default"));
    }

    @Test
    public void shouldHandleRelativePathWithPrecedingSlash() {
        assertEquals("/foo", requestParser.parseRelativeURI("/foo", "default"));
    }

    @Test
    public void shouldHandleRelativePathWithTrailingSlash() {
        assertEquals("foo/", requestParser.parseRelativeURI("foo/", "default"));
    }

    @Test
    public void shouldHandleInvalidURL() {
        assertEquals("default", requestParser.parseRelativeURI("ftp://www.GOOGLE.co.uk/foo/bar", "default"));
    }

    @Test
    public void shouldHandleLocalhostURL() {
        assertEquals("/leagueTable", requestParser.parseRelativeURI("https://localhost:8443/leagueTable", "default"));
    }

}
