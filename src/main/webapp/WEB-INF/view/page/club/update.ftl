<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Update Club</title>
</#macro>

<#macro content_header>
<div id="header">Update Club</div>
</#macro>

<#macro content_section>
<form action="/club/update" method="POST">
    <@errors.print_errors "club"/>

</form>
</#macro>

<@page_html/>
