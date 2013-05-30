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

    #division:invalid.filled + .error_message::after {
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
            <label for="name">Start Date:</label><@spring.formInput path="round.startDate" fieldType="date" attributes='required="required" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"' />
            <span class="error_message"></span>
        </p>

        <p>
            <label for="name">End Date:</label><@spring.formInput path="round.endDate" fieldType="date" attributes='required="required" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"' />
            <span class="error_message"></span>
        </p>

        <p>
            <label for="name">Status:</label><input id="id" type="text" value="${round.status!""}" readonly="readonly">
        </p>

        <p class="select">
            <label class="division" for="division">League:</label>
            <#if (divisions?size > 0)>
                <select id="division" name="division" required="required" title="${environment.getProperty("validation.round.division")}">
                    <option value="">${environment.getProperty("message.general.please_select")}</option>
                    <#list divisions as division>
                        <option value="${division.id}" <#if (round.division?? && round.division.id == division.id)>selected="selected"</#if>>${division.name}</option>
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
                changed: ['division'],
                onload: errors && errors.round
            };
</script>
</#macro>

<@page_html/>
