<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - ${title!"Message"}</title>
</#macro>

<#macro content_header>
<div id="header">${title!"Message"}</div>
</#macro>

<#macro content_section>
<div class="section">
    <#if !(print?? && print)>
        <div class="standalone_link"><#if showAllDivisions?? && !showAllDivisions><a href="/leagueTable?showAllDivisions=true">View Leagues</a><#else><a href="/leagueTable">View Divisions</a></#if></div>
        <div class="standalone_link"><a href="/account#matches">Contact your opponents</a></div>
    </#if>
    <#if (user.rounds?size > 0)>
        <#assign dateHash = user.rounds[0].startDate.hashCode() + "_" + user.rounds[0].endDate.hashCode() />
        <h2 class="table_title">(${user.rounds[0].startDate.toDate()?string("dd MMM yyyy")} &ndash; ${user.rounds[0].endDate.toDate()?string("dd MMM yyyy")})</h2>
        <#list user.rounds as round>
            <#if dateHash != round.startDate.hashCode() + "_" + round.endDate.hashCode()>
                <#assign dateHash = round.startDate.hashCode() + "_" + round.endDate.hashCode() />
                <h2 class="table_title page_break" style="margin-top: 2em;">(${round.startDate.toDate()?string("dd MMM yyyy")} &ndash; ${round.endDate.toDate()?string("dd MMM yyyy")})</h2>
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
                            <@matchCell match/>
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
    <#else>
        <div class="errors_messages"  style="text-align: left; margin: 5em auto;">
            <p style="margin: 0.25em;">You are not currently in any rounds for one of the following reasons:</p>
            <ul style="margin: 1em; list-style-image: none; list-style-position: outside; list-style-type: disc;">
                <li style="margin-left: 2em;">you are not an active player in any leagues</li>
                <li style="margin-left: 2em; margin-top: .25em;">you have recently become an active player in a league but a new round has not started yet</li>
            </ul>
            <p style="margin: 0.25em;">For more details use the <a href="/account">account</a> page to check which leagues you are registered for</p>
        </div>
    </#if>
    <#if print?? && print><script>window.print();</script></#if>
</#macro>

<#macro matchCell match>
    <td style="white-space: nowrap;<#if match.isMyMatch(user) > color: #000000;</#if>"><#if match.score?? >${match.score}<#elseif match.isMyMatch(user) ><a href="/score/${match.id}">enter</a></#if></td>
</#macro>


<@page_html/>