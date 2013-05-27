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
        <#list rounds as round>
            <h2 class="table_title">${round.division.league.club.name} &ndash; ${round.division.league.name} &ndash; ${round.division.name}</h2>

            <h2 class="table_subtitle">(${round.startDate.toDate()?string("dd MMM yyyy")} &ndash; ${round.endDate.toDate()?string("dd MMM yyyy")})</h2>
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
                        <th class="player">Andrea Caldera</th>
                        <th class="player">Paul Taylor</th>
                        <th class="player">Benoit Obadia</th>
                        <th class="player">David Beguier-Barnett</th>
                        <th class="player">John KEEN</th>
                    </tr>
                    <tr>
                        <td class="player">Andrea Caldera</td>
                        <td class="no_match">X</td>
                        <td class="score"></td>
                        <td class="score"></td>
                        <td class="score"></td>
                        <td class="score"></td>
                    </tr>
                    <tr>
                        <td class="player">Paul Taylor</td>
                        <td class="score"></td>
                        <td class="no_match">X</td>
                        <td class="score"></td>
                        <td class="score"></td>
                        <td class="score"></td>
                    </tr>
                    <tr>
                        <td class="player">Benoit Obadia</td>
                        <td class="score"></td>
                        <td class="score"></td>
                        <td class="no_match">X</td>
                        <td class="score"></td>
                        <td class="score">0-3</td>
                    </tr>
                    <tr>
                        <td class="player">David Beguier-Barnett</td>
                        <td class="score"></td>
                        <td class="score"></td>
                        <td class="score"></td>
                        <td class="no_match">X</td>
                        <td class="score"></td>
                    </tr>
                    <tr>
                        <td class="player">John KEEN</td>
                        <td class="score"></td>
                        <td class="score"></td>
                        <td class="score">3-0</td>
                        <td class="score"></td>
                        <td class="no_match">X</td>
                    </tr>
                </tbody>
            </table>
        </#list>
    </#if>

</#macro>



<@page_html/>