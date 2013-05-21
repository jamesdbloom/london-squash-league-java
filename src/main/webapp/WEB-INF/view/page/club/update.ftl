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
    <div class="delete_sessions_form"><input name="type" type="hidden" value="club">
        <p><label class="id" for="id">Club Id:</label><input id="id" name="id" type="text" value="${club.id!""}" readonly="readonly"></p>

        <p><label class="name" for="name">Name:</label><@spring.formInput  path="club.name" attributes="required='required' pattern='.{5,25}' title='${environment.getProperty(\"validation.club.name\")}' class='show_validation'"/></p>

        <p><label class="address" for="address">Address:</label><@spring.formInput  path="club.address" attributes="required='required' pattern='.{5,50}' title='${environment.getProperty(\"validation.club.address\")}' class='show_validation'"/></p>

        <p class="submit"><input class="submit" type="submit" name="save" value="save"></p>
    </div>
</form><br>
</#macro>

<@page_html/>
