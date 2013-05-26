<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - ${title!"Message"}</title>
</#macro>

<#macro content_header>
<div id="header">${title!"Message"}</div>
</#macro>

<#macro content_section>
<p <#if error?? && error>class="error_message"<#else>class="message"</#if>>${message!""}</p>
</#macro>

<@page_html/>