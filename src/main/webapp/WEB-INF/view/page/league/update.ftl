<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Update League</title>
</#macro>

<#macro content_header>
<div id="header">Update League</div>
</#macro>

<#macro content_section>
<style>
    #name:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.league.name")}";
    }

    #club:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.league.club")}";
    }
</style>
<form action="/league/update" method="POST">

    <@errors.print_errors "league"/>
    <div class="standard_form">

        <p>
            <label class="id" for="id">Id:</label><input id="id" name="id" type="text" value="${league.id!""}" readonly="readonly">
        </p>
        <input id="version" name="version" type="hidden" value="${league.version!""}">

        <p>
            <label for="name">Name:</label><@spring.formInput path="league.name" attributes='required="required" pattern=".{5,25}" maxlength="25" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"' />
            <span class="error_message"></span>
        </p>

        <p class="select">
            <label class="club" for="club">Club:</label>
            <#if (clubs?size > 0)>
                <select id="club" name="club" required="required" title="${environment.getProperty("validation.league.club")}">
                    <option value="">${environment.getProperty("message.general.please_select")}</option>
                    <#list clubs as club>
                        <option value="${club.id}" <#if (league.club?? && league.club.id == club.id)>selected="selected"</#if>>${club.name}</option>
                    </#list>
                </select> <span class="error_message"></span>
            </#if>
        </p>

        <p class="submit">
            <input class="submit primary" type="submit" name="update" value="Update">
        </p>
    </div>
</form>
<script>
    var errors = errors || {},
            validation = {
                filled: ['name'],
                changed: ['club'],
                onload: errors && errors.league
            };
</script>
</#macro>

<@page_html/>
