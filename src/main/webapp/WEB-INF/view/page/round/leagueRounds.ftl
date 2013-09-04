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

        <p class="select">
            <label for="previousRoundId">Previous Round:</label>
            <#if (rounds?size > 0)>
                <select id="previousRoundId" name="previousRoundId" required="required" title="${environment.getProperty("validation.round.previousRound")}">
                    <option value="">${environment.getProperty("message.general.please_select")}</option>
                    <#list rounds as round>
                        <option value="${round.id}" <#if (previousRoundId?? && previousRoundId == round.id)>selected="selected"</#if>>${round.league.club.name} &ndash; ${round.league.name} &ndash; (${round.startDate.toDate()?string("dd MMM yyyy")} - ${round.endDate.toDate()?string("dd MMM yyyy")})</option>
                    </#list>
                </select> <span class="error_message"></span>
            </#if>
        </p>

        <p class="submit">
            <input class="submit primary" type="submit" value="Create Round">
        </p>
    </div>
    <#if (rounds?size > 0)>
    </form><h2 class="table_title">Existing Rounds</h2>
        <#assign club_league_id = rounds[0].league.club.id + "_" + rounds[0].league.id />
    <h2 class="table_subtitle page_break">${rounds[0].league.club.name} &ndash; ${rounds[0].league.name}</h2>
    <form action="/leagueRounds/createMatches" method="POST">
    <table class="action_table">
    <tbody class="strip_rows">
        <tr>
            <th style="width: 35%;">Status</th>
            <th>Start</th>
            <th>End</th>
            <th class="button_column last"></th>
        </tr>
        <#list rounds as round>
            <#if club_league_id != round.league.club.id + "_" + round.league.id>
                <#assign club_league_id = round.league.club.id + "_" + round.league.id />
            </tbody>
            </table>
                <h2 class="table_subtitle page_break" style="margin-top: 2em;">${round.league.club.name} &ndash; ${round.league.name}</h2>
            <table class="action_table">
            <tbody class="strip_rows">
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
                <td class="button_column last"><a class="button" href="/leagueRounds/update/${round.id}">Modify</a><#if round.notStarted() && (round.divisions?size == 0)>
                    <button class="submit button" type="submit" name="roundId" value="${round.id}" title="Create Matches">Create Matches</button></#if></td>
            </tr>
        </#list>
    </tbody>
    </table>
    </form>
    <#else>
    <div id="no_rounds" class="errors_messages" style="margin: 5em auto;">
        <p style="margin: 0.25em 0;">You are not currently in any rounds for one of the following reasons:</p>
        <ul style="margin: 1em; list-style-image: none; list-style-position: outside; list-style-type: disc;">
            <li style="margin-left: 2em;">you are not an active player in any leagues,</li>
            <li style="margin-left: 2em; margin-top: .25em;">you have recently become an active player in a league but a new round has not started yet.</li>
        </ul>
        <p style="margin: 0.75em 0;">For more details use the <a href="/account">account</a> page to check which leagues you are registered for.</p>

        <p style="margin: 0.25em 0;">To view all rounds for the leagues you are a player in (including those you do not have a match in) use the <a href="/leagueTable?showAllDivisions=true">View Leagues</a> button above.</p>
    </div>
    </#if>
<script>
    var errors = errors || {},
            validation = {
                filled: ['startDate', 'endDate'],
                changed: ['leagueId', 'previousRoundId'],
                onload: errors && errors.round
            };
</script>
</#macro>

<@page_html/>