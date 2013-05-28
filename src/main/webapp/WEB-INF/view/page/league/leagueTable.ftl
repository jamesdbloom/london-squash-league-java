<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - ${title!"Message"}</title>
</#macro>

<#macro content_header>
<div id="header">${title!"Message"}</div>
</#macro>

<#macro content_section>
<div class="section">
    <div class="message">
        <div class="table_message">This page shows the divisions you are playing in, to view all divisions in one of your leagues select a league:</div>
        <form method="get" action="/leagueTable">
            <div class="select_league_form">
                <div class="select"><select name="league_id">
                    <option value="1">Hammersmith GLL &ndash; Evening</option>
                    <option value="2">Hammersmith GLL &ndash; Lunchtime</option>
                </select></div>
                <input type="hidden" name="print" value="true"><input type="hidden" name="finished" value="false">

                <div><input class="submit" type="submit" value="select"></div>
            </div>
        </form>
    </div>
    <div class="standalone_link"><a href="/leagueTable?finished=true">Show finished rounds</a></div>
    <div class="standalone_link"><a href="/account#matches">Contact your opponents</a></div>

    <#if (rounds?size > 0)>
        <#assign dateHash = rounds[0].startDate.hashCode() + "_" + rounds[0].endDate.hashCode() />
        <h2 class="table_title">(${rounds[0].startDate.toDate()?string("dd MMM yyyy")} &ndash; ${rounds[0].endDate.toDate()?string("dd MMM yyyy")})</h2>
        <#list rounds as round>
            <#if dateHash != round.startDate.hashCode() + "_" + round.endDate.hashCode()>
                <#assign dateHash = round.startDate.hashCode() + "_" + round.endDate.hashCode() />
                <h2 class="table_title" style="margin-top: 2em;">(${round.startDate.toDate()?string("dd MMM yyyy")} &ndash; ${round.endDate.toDate()?string("dd MMM yyyy")})</h2>
            </#if>
            <h2 class="table_subtitle">${round.division.league.club.name} &ndash; ${round.division.league.name} &ndash; ${round.division.name}</h2>
            <table class="small_screen">
                <tbody>
                    <tr>
                        <th>Player One</th>
                        <th>Player Two</th>
                        <th>Score</th>
                    </tr>
                    <#list round.matches as match>
                        <tr>
                            <td>${match.playerOne.user.name}</td>
                            <td>${match.playerTwo.user.name}</td>
                            <td style="white-space: nowrap"><#if match.score?? >${match.score}<#elseif match.isMyMatch(user) ><a href="/score/${match.id}">enter</a></#if></td>
                        </tr>
                    </#list>
                </tbody>
            </table>
            <table class="league large_screen">
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
                                        <#local match = round.getMatch(playerRow.id, playerColumn.id) />
                                        <td style="white-space: nowrap"><#if match.score?? >${match.score}<#elseif match.isMyMatch(user) ><a href="/score/${match.id}">enter</a></#if></td>
                                    <#elseif round.getMatch(playerColumn.id, playerRow.id)?? >
                                        <#local match = round.getMatch(playerColumn.id, playerRow.id) />
                                        <td style="white-space: nowrap"><#if match.score?? >${match.score}<#elseif match.isMyMatch(user) ><a href="/score/${match.id}">enter</a></#if></td>
                                    <#else>
                                        <td style="white-space: nowrap">OOPS ${playerColumn.id} ${playerRow.id}</td>
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

</#macro>



<@page_html/>