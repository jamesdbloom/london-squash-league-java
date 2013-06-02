<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - ${title!"Message"}</title>
</#macro>

<#macro content_header>
<div id="header">${title!"Message"}</div>
</#macro>

<#macro content_section>
<div class="section">
    <#if (rounds?size > 0)>
        <#assign dateHash = rounds[0].startDate.hashCode() + "_" + rounds[0].endDate.hashCode() />
        <h2 class="table_title">(${rounds[0].startDate.toDate()?string("dd MMM yyyy")} &ndash; ${rounds[0].endDate.toDate()?string("dd MMM yyyy")})</h2>
        <#list rounds as round>
            <#if dateHash != round.startDate.hashCode() + "_" + round.endDate.hashCode()>
                <#assign dateHash = round.startDate.hashCode() + "_" + round.endDate.hashCode() />
                <h2 class="table_title page_break" style="margin-top: 2em;">(${round.startDate.toDate()?string("dd MMM yyyy")} &ndash; ${round.endDate.toDate()?string("dd MMM yyyy")})</h2>
            </#if>
            <h2 class="table_subtitle">${round.division.league.club.name} &ndash; ${round.division.league.name} &ndash; ${round.division.name}</h2>
            <table>
                <tbody>
                    <tr>
                        <th class="player"></th>
                        <#list round.players as playerColumn>
                            <th>${playerColumn.user.name}</th>
                        </#list>
                    </tr>
                    <#list round.players as playerRow>
                        <tr>
                            <td>${playerRow.user.name}</td>
                            <#list round.players as playerColumn>
                                <#if playerRow.id != playerColumn.id >
                                    <#if round.getMatch(playerRow.id, playerColumn.id)?? >
                                        <@matchCell round.getMatch(playerRow.id, playerColumn.id)/>
                                    <#elseif round.getMatch(playerColumn.id, playerRow.id)?? >
                                        <@matchCell round.getMatch(playerColumn.id, playerRow.id)/>
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