<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Create Round & Matches</title>
</#macro>

<#macro content_header>
<div id="header">Leagues</div>
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
        content: "${environment.getProperty("validation.round.league")}";
    }
</style><h2 class="table_title">Create Round & Matches</h2>
<form action="/leagueRounds/create" method="POST">
    <@errors.print_errors_list "round"/>
    <div class="standard_form" style="margin-bottom: 0;">

        <p>
            <label for="startDate">Start Date:</label><input type="date" id="startDate" name="startDate" value="${startDate!""}" required="required" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"/> <span class="error_message"></span>
        </p>

        <p>
            <label for="endDate">End Date:</label><input type="date" id="endDate" name="endDate" value="${endDate!""}" required="required" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"/> <span class="error_message"></span>
        </p>

        <p class="select">
            <label for="leagueId">League:</label>
            <#if (leagues?size > 0)>
                <select id="leagueId" name="leagueId" required="required" title="${environment.getProperty("validation.round.league")}">
                    <option value="">${environment.getProperty("message.general.please_select")}</option>
                    <#list leagues as league>
                        <option value="${league.id}" <#if (leagueId?? && leagueId == league.id)>selected="selected"</#if>>${league.club.name} &ndash; ${league.name}</option>
                    </#list>
                </select> <span class="error_message"></span>
            </#if>
        </p>

        <p class="submit">
            <input class="submit primary" type="submit" value="Create Round">
        </p>
    </div>
</form><h2 class="table_title">Existing Rounds</h2>
    <#assign club_league_id = rounds[0].division.league.club.id + "_" + rounds[0].division.league.id />
<h2 class="table_subtitle page_break">${rounds[0].division.league.club.name} &ndash; ${rounds[0].division.league.name}</h2>
<table class="action_table">
<tbody>
    <tr>
        <th style="width: 35%;">Status</th>
        <th>Start</th>
        <th>End</th>
        <th class="button_column last"></th>
    </tr>
    <#list rounds as round>
        <#if club_league_id != round.division.league.club.id + "_" + round.division.league.id>
            <#assign club_league_id = round.division.league.club.id + "_" + round.division.league.id />
            </tbody>
            </table>
            <table class="action_table">
                <h2 class="table_subtitle page_break" style="margin-top: 2em;">${round.division.league.club.name} &ndash; ${round.division.league.name}</h2>
            <tbody>
                <tr>
                    <th style="width: 35%;">Status</th>
                    <th>Start</th>
                    <th>End</th>
                    <th class="button_column last"></th>
                </tr>
        </#if>
        <tr>
            <td>${round.status}</td>
            <td>${round.startDate.toDate()?string("dd MMM yyyy")}</td>
            <td>${round.endDate.toDate()?string("dd MMM yyyy")}</td>
            <td class="button_column last"><a class="button" href="/leagueRounds/update/${round.id}">Modify</a></td>
        </tr>
    </#list>
</tbody>
</table>
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