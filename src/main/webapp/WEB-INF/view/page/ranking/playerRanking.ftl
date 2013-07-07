<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Create Round & Matches</title>
</#macro>

<#macro content_header>
<div id="header">Leagues</div>
</#macro>

<#macro content_section>
    <#if (rounds?size > 0)>
        <#list rounds as round>
        <h2 class="table_sub_title page_break" style="margin-top: 1em; margin-bottom: 1em;">${round.league.club.name} &ndash; ${round.league.name} &ndash; (${round.startDate.toDate()?string("dd MMM yyyy")} &ndash; ${round.endDate.toDate()?string("dd MMM yyyy")})</h2>
        <table>
            <tbody>
                <tr>
                    <th>Player</th>
                    <th>Points</th>
                </tr>
                <#assign values = round.playerSortedByScore?values>
                <#list round.playerSortedByScore?keys as player>
                    <tr>
                        <td>${player.user.name}</td>
                        <td>${values[player_index]}</td>
                    </tr>
                </#list>
            </tbody>
        </table>
        </#list>
    <#else>
    <div id="no_rounds" class="errors_messages" style="margin: 5em auto;">
        <p style="margin: 0.25em 0;">There are currently no rounds for this club...</p>
    </div>
    </#if>
</#macro>

<@page_html/>