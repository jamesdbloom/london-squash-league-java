<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Update Club</title>
</#macro>

<#macro content_header>
<div id="header">Update Club</div>
</#macro>

<#macro content_section>
<style>
    .error_message::after {
        float: right;
        width: 60%;
        margin: 1em;
        text-align: center;
        border: 1px dashed rgba(255, 97, 97, 0.51);
        padding: 0.5em;
    }
</style>
<style>
    #name:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.club.name")}";
    }

    #address:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.club.address")}";
    }
</style>
<form action="/club/update" method="POST" novalidate="novalidate">
    <#assign filledFunction = "if(this.value.length >= 1){ this.setAttribute('class', 'filled'); } else { this.setAttribute('class', 'empty'); }" />

    <@errors.print_errors "club"/>
    <div class="standard_form"><input name="type" type="hidden" value="club">

        <p>
            <label class="id" for="id">Id:</label><input id="id" name="id" type="text" value="${club.id!""}" readonly="readonly">
        </p>
        <input id="version" name="version" type="hidden" value="${club.version!""}">

        <p>
            <label class="name" for="name">Name:</label><@spring.formInput  path="club.name" attributes='required="required" pattern=".{5,25}" maxlength="25" title="${environment.getProperty("validation.club.name")}" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off" onblur="${filledFunction}"'/>
            <span class="error_message"></span>
        </p>

        <p>
            <label class="address" for="address">Address:</label><@spring.formInput  path="club.address" attributes='required="required" pattern=".{5,50}" maxlength="50" title="${environment.getProperty("validation.club.address")}" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off" onblur="${filledFunction}"'/>
            <span class="error_message"></span>
        </p>

        <p class="submit"><input class="submit" type="submit" name="update" value="update"></p>
    </div>
</form><br>
</#macro>

<@page_html/>
