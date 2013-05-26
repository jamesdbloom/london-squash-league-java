<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Update Division</title>
</#macro>

<#macro content_header>
<div id="header">Update Division</div>
</#macro>

<#macro content_section>
<style>
    #name:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.division.name")}";
    }

    #league:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.division.league")}";
    }
</style>
<form action="/division/update" method="POST">

    <@errors.print_errors "division"/>
    <div class="standard_form">

        <p>
            <label class="id" for="id">Id:</label><input id="id" name="id" type="text" value="${division.id!""}" readonly="readonly">
        </p>
        <input id="version" name="version" type="hidden" value="${division.version!""}">

        <p>
            <label for="name">Name:</label><@spring.formInput path="division.name" attributes='required="required" pattern=".{5,25}" maxlength="25" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"' />
            <span class="error_message"></span>
        </p>

        <p class="select">
            <label class="league" for="league">League:</label>
            <#if (leagues?size > 0)>
                <select id="league" name="league" required="required" title="${environment.getProperty("validation.division.league")}">
                    <option value="">${environment.getProperty("message.general.please_select")}</option>
                    <#list leagues as league>
                        <option value="${league.id}" <#if (division.league?? && division.league.id == league.id)>selected="selected"</#if>>${league.name}</option>
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
                changed: ['league'],
                onload: errors && errors.division
            };
</script>
</#macro>

<@page_html/>
