<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Update Player</title>
</#macro>

<#macro content_header>
<div id="header">Update Player</div>
</#macro>

<#macro content_section>
<style>
    #user:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.player.user")}";
    }

    #currentDivision:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.player.currentDivision")}";
    }

    #league:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.player.league")}";
    }

    #status:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.player.status")}";
    }
</style>
<form action="/player/update" method="POST">

    <@errors.print_binding_errors "player"/>
    <div class="standard_form">

        <p>
            <label class="id" for="id">Id:</label><input id="id" name="id" type="text" value="${player.id!""}" readonly="readonly">
        </p>
        <input id="version" name="version" type="hidden" value="${player.version!""}">

        <p class="select">
            <label for="user">User:</label>
            <#if (users?size > 0)>
                <select id="user" name="user" required="required" title="${environment.getProperty("validation.player.user")}">
                    <option value="">${environment.getProperty("message.general.please_select")}</option>
                    <#list users as user>
                        <option value="${user.id}" <#if (player.user?? && player.user.id == user.id)>selected="selected"</#if>>${user.name}</option>
                    </#list>
                </select> <span class="error_message"></span>
            </#if>
        </p>

        <p class="select">
            <label for="currentDivision">Division:</label>
            <#if (divisions?size > 0)>
                <select id="currentDivision" name="currentDivision" required="required" title="${environment.getProperty("validation.player.currentDivision")}">
                    <option value="">${environment.getProperty("message.general.please_select")}</option>
                    <#list divisions as division>
                        <option value="${division.id}" <#if (player.currentDivision?? && player.currentDivision.id == division.id)>selected="selected"</#if>>${division.league.club.name} &ndash; ${division.league.name} &ndash; ${division.name}</option>
                    </#list>
                </select> <span class="error_message"></span>
            </#if>
        </p>

        <p class="select">
            <label for="league">League:</label>
            <#if (leagues?size > 0)>
                <select id="league" name="league" required="required" title="${environment.getProperty("validation.player.league")}">
                    <option value="">${environment.getProperty("message.general.please_select")}</option>
                    <#list leagues as league>
                        <option value="${league.id}" <#if (player.league?? && player.league.id == league.id)>selected="selected"</#if>>${league.club.name} &ndash; ${league.name}</option>
                    </#list>
                </select> <span class="error_message"></span>
            </#if>
        </p>

        <p class="select">
            <label for="status">Status:</label>
            <@spring.bind "playerStatuses" />
            <@spring.formSingleSelectWithEmpty path="player.status" options=playerStatuses emptyValueMessage='${environment.getProperty("message.general.please_select")}' attributes='required="required"'/>
            <span class="error_message"></span>
        </p>

        <p class="submit">
            <input class="submit primary" type="submit" name="update" value="Update">
        </p>
    </div>
</form>
<script>
    var errors = errors || {},
            validation = {
                changed: ['user', 'currentDivision', 'league', 'status'],
                onload: errors && errors.player
            };
</script>
</#macro>

<@page_html/>
