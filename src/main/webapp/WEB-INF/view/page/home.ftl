<#include "/WEB-INF/view/layout/default.ftl" />
<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />

<#macro page_title>
<title>London Squash League - Home</title>
</#macro>

<#macro content_header>
<div id="header">Home</div>
</#macro>

<#macro content_section>

    <@security.authorize access='isAnonymous()'>
    <p class="homepage_message">Join the league at your club in two easy steps:</p>
    <ol class="link_list">
        <li><a href="/register">1. Register</a></li>
        <li><a href="/account#leagues">2. Join A League</a></li>
    </ol>
    <div style="width:100%; height: 3.5em;"></div><p class="homepage_message">Or existing members....</p>
    <ol class="link_list">
        <li><a href="/login">Login</a></li>
        <li><a href="/retrievePassword">Lost Password?</a></li>
    </ol>
    </@security.authorize>

    <@security.authorize access='isAuthenticated()'>
    <p class="homepage_message">${user.name?capitalize} please select one of the following:</p>
    <ol class="link_list">
        <li><a href="/leagueTable">View Your Leagues</a></li>
        <li><a href="/ranking">View Player Ranking</a></li>
        <li><a href="/account#matches">Enter Match Score</a></li>
        <li><a href="/account#leagues">Join A New League</a></li>
        <li><a href="/account">Account Settings</a></li>
        <li><a href="/contact_us">Contact Us</a></li>
        <li><a href="/logout">Logout</a></li>
    </ol>
    </@security.authorize>
    <@security.authorize ifAnyGranted="ROLE_ADMIN">
    <div style="width:100%; height: 3.5em;"></div><p class="homepage_message">Administration tasks:</p>
    <ol class="link_list">
        <li><a href="/print">Print League</a></li>
        <li><a href="/leagueRounds">Create Round</a></li>
        <li><a href="/administration">Administration</a></li>
    </ol>
    </@security.authorize>
</#macro>

<@page_html/>

