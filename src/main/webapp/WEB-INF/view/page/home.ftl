<#include "/WEB-INF/view/layout/default.ftl" />
<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />

<#macro page_title>
<title>London Squash League - Leagues</title>
</#macro>

<#macro content_header>
<div id="header">Leagues</div>
</#macro>

<#macro content_section>
<ol class="link_list">
    <@security.authorize access='isAnonymous()'>
        <li><a href="/login" title="Login">Login</a></li>
        <li><a href="/register" title="Register">Register</a></li>
        <li><a href="/retrieve_password" title="Lost Password?">Lost Password?</a></li>
    </@security.authorize>

    <@security.authorize access='isAuthenticated()'>
        <#if user??>
            <p>Hello ${user.name}</p>
        </#if>

        <@security.authorize ifAnyGranted="ROLE_ADMIN">
            <li><a href="/print" title="Print League">Print League</a></li>
        </@security.authorize>
        <li><a href="/league" title="View League">View Your League</a></li>
        <li><a href="/ranking" title="Player Ranking">Player Ranking</a></li>
        <li><a href="/score" title="Enter Match Score">Enter Match Score</a></li>
        <li><a href="/user/update/${user.id}" title="Account">Account Settings</a></li>
        <li><a href="/account#divisions" title="Join A New League">Join A New League</a></li>
        <@security.authorize ifAnyGranted="ROLE_ADMIN">
            <li><a href="/administration" title="Administration">Administration</a></li>
        </@security.authorize>
        <li><a href="/contact_us" title="Contact Us">Contact Us</a></li>
        <li><a href="/logout" title="Logout">Logout</a></li>
    </@security.authorize>
</ol>
</#macro>

<@page_html/>

