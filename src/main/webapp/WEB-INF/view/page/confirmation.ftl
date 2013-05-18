<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Message Sent</title>
</#macro>

<#macro content_header>
<div id="header">Message Sent</div>
</#macro>

<#macro content_section>
<p class="message">You message has been sent, a copy of your message has been sent to ${user.email}</p>
</#macro>

<@page_html/>