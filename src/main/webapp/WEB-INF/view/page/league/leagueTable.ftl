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
    <#if (user.divisions?size > 0)>
        <#assign dateHash = user.divisions[0].round.startDate.hashCode() + "_" + user.divisions[0].round.endDate.hashCode() />
        <h2 class="table_title">(${user.divisions[0].round.startDate.toDate()?string("dd MMM yyyy")} &ndash; ${user.divisions[0].round.endDate.toDate()?string("dd MMM yyyy")})</h2>
        <#list user.divisions as division>
            <#if dateHash != division.round.startDate.hashCode() + "_" + division.round.endDate.hashCode()>
                <#assign dateHash = division.round.startDate.hashCode() + "_" + division.round.endDate.hashCode() />
                <h2 class="table_title page_break" style="margin-top: 2em;">(${division.round.startDate.toDate()?string("dd MMM yyyy")} &ndash; ${division.round.endDate.toDate()?string("dd MMM yyyy")})</h2>
            </#if>
            <h2 class="table_subtitle">${division.round.league.club.name} &ndash; ${division.round.league.name} &ndash; ${division.round.league.name}</h2>
            <table class="small_screen">
                <tbody>
                    <tr>
                        <th>Player One</th>
                        <th>Player Two</th>
                        <th>Score</th>
                    </tr>
                    <#list division.matches as match>
                        <tr>
                            <td id="match_${division_index}_${match_index}_smallScreenPlayerOne">${match.playerOne.user.name}</td>
                            <td id="match_${division_index}_${match_index}_smallScreenPlayerTwo">${match.playerTwo.user.name}</td>
                            <@matchCell match true/>
                        </tr>
                    </#list>
                </tbody>
            </table>
            <table class="league large_screen">
                <tbody>
                    <tr>
                        <th class="player"></th>
                        <#list division.players as playerColumn>
                            <th id="match_${division_index}_${playerColumn_index}_largeScreenPlayerOne">${playerColumn.user.name}</th>
                        </#list>
                    </tr>
                    <#list division.players as playerRow>
                        <tr>
                            <td id="match_${division_index}_${playerRow_index}_largeScreenPlayerTwo">${playerRow.user.name}</td>
                            <#list division.players as playerColumn>
                                <#if playerRow.id != playerColumn.id >
                                    <#if division.getMatch(playerRow.id, playerColumn.id)?? >
                                        <@matchCell division.getMatch(playerRow.id, playerColumn.id) false/>
                                    <#elseif division.getMatch(playerColumn.id, playerRow.id)?? >
                                        <@matchCell division.getMatch(playerColumn.id, playerRow.id) false/>
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
        <div id="no_rounds" class="errors_messages"  style="margin: 5em auto;">
            <p style="margin: 0.25em 0;">You are not currently in any rounds for one of the following reasons:</p>
            <ul style="margin: 1em; list-style-image: none; list-style-position: outside; list-style-type: disc;">
                <li style="margin-left: 2em;">you are not an active player in any leagues,</li>
                <li style="margin-left: 2em; margin-top: .25em;">you have recently become an active player in a league but a new round has not started yet.</li>
            </ul>
            <p style="margin: 0.75em 0;">For more details use the <a href="/account">account</a> page to check which leagues you are registered for.</p>
            <p style="margin: 0.25em 0;">To view all rounds for the leagues you are a player in (including those you do not have a match in) use the <a href="/leagueTable?showAllDivisions=true">View Leagues</a> button above.</p>
        </div>
    </#if>
    <#if print?? && print><script>window.print();</script></#if>
</#macro>

<#macro matchCell match smallScreen>
    <td  id="match_${match.playerOne.id}_${match.playerTwo.id}_<#if smallScreen>smallScreen<#else>largeScreen</#if>Score" style="white-space: nowrap;<#if match.isMyMatch(user) > color: #000000;</#if>"><#if match.score?? >${match.score}<#elseif match.isMyMatch(user) ><a href="/score/${match.id}">enter</a></#if></td>
</#macro>


<@page_html/>