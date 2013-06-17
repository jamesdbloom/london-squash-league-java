<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - ${title!"Message"}</title>
</#macro>

<#macro content_header>
<div id="header">${title!"Message"}</div>
</#macro>

<#macro content_section>
<div class="section">
    <#if (user.divisions?size > 0)>
        <#assign dateHash = user.divisions[0].round.startDate.hashCode() + "_" + user.divisions[0].round.endDate.hashCode() />
        <h2 class="table_title">(${user.divisions[0].round.startDate.toDate()?string("dd MMM yyyy")} &ndash; ${user.divisions[0].round.endDate.toDate()?string("dd MMM yyyy")})</h2>
        <#list user.divisions as division>
            <#if dateHash != division.round.startDate.hashCode() + "_" + division.round.endDate.hashCode()>
                <#assign dateHash = division.round.startDate.hashCode() + "_" + division.round.endDate.hashCode() />
                <h2 class="table_title page_break" style="margin-top: 2em;">(${division.round.startDate.toDate()?string("dd MMM yyyy")} &ndash; ${division.round.endDate.toDate()?string("dd MMM yyyy")})</h2>
            </#if>
            <h2 class="table_subtitle">${division.round.league.club.name} &ndash; ${division.round.league.name} &ndash; ${division.round.league.name}</h2>
            <table>
                <tbody>
                    <tr>
                        <th class="player"></th>
                        <#list division.players as playerColumn>
                            <th>${playerColumn.user.name}</th>
                        </#list>
                    </tr>
                    <#list division.players as playerRow>
                        <tr>
                            <td>${playerRow.user.name}</td>
                            <#list division.players as playerColumn>
                                <#if playerRow.id != playerColumn.id >
                                    <#if division.getMatch(playerRow.id, playerColumn.id)?? >
                                        <@matchCell division.getMatch(playerRow.id, playerColumn.id)/>
                                    <#elseif division.getMatch(playerColumn.id, playerRow.id)?? >
                                        <@matchCell division.getMatch(playerColumn.id, playerRow.id)/>
                                    </#if>
                                <#else>
                                    <td style="white-space: nowrap">X</td>
                                </#if>
                            </#list>
                        </tr>
                    </#list>
                </tbody>
            </table>
        </#list>
    </#if>
    <script>window.print();</script>
</#macro>

<#macro matchCell match>
    <td style="white-space: nowrap;"><#if match.score?? >${match.score}</#if></td>
</#macro>


<@page_html/>