<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Update Round</title>
</#macro>

<#macro content_header>
<div id="header">Update Round</div>
</#macro>

<#macro content_section>
<style>
    #startDate:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.round.startDate")}";
    }

    #endDate:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.round.endDate")}";
    }

    #league:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.round.division")}";
    }
</style>
<form action="/round/update" method="POST">

    <@errors.print_binding_errors "round"/>
    <div class="standard_form">

        <p>
            <label class="id" for="id">Id:</label><input id="id" name="id" type="text" value="${round.id!""}" readonly="readonly">
        </p>
        <input id="version" name="version" type="hidden" value="${round.version!""}">

        <p>
            <label for="startDate">Start Date:</label><@spring.formInput path="round.startDate" fieldType="date" attributes='required="required" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"' />
            <span class="error_message"></span>
        </p>

        <p>
            <label for="endDate">End Date:</label><@spring.formInput path="round.endDate" fieldType="date" attributes='required="required" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"' />
            <span class="error_message"></span>
        </p>

        <p>
            <label for="status">Status:</label><input id="status" type="text" value="${round.status!""}" readonly="readonly">
        </p>

        <p class="select">
            <label class="league" for="league">Division:</label>
            <#if (leagues?size > 0)>
                <select id="league" name="league" required="required" title="${environment.getProperty("validation.round.division")}">
                    <option value="">${environment.getProperty("message.general.please_select")}</option>
                    <#list leagues as league>
                        <option value="${league.id}" <#if (round.league?? && round.league.id == league.id)>selected="selected"</#if>>${league.name}</option>
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
                filled: ['startDate', 'endDate'],
                changed: ['league'],
                onload: errors && errors.round
            };
</script>
</#macro>

<@page_html/>
