<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - ${title}</title>
</#macro>

<#macro content_header>
<div id="header">${title}</div>
</#macro>

<#macro content_section>
<p class="message">${message}</p>
</#macro>

<@page_html/>