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
        <h2 class="table_sub_title page_break" style="margin-top: 1em; margin-bottom: 1em;">${round.league.club.name} &ndash; ${round.league.name}<span class="hide_on_small_screen"> &ndash; </span><br class="display_on_small_screen"/> (${round.startDate.toDate()?string("dd MMM yyyy")} &ndash; ${round.endDate.toDate()?string("dd MMM yyyy")})</h2>
        <table>
            <tbody class="strip_rows">
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
    <div class="errors_messages" style="margin: 5em auto;">
        <p style="margin: 0.25em 0;">The points are calculated as follows:</p>
        <ul style="margin: 1em; list-style-image: none; list-style-position: outside; list-style-type: disc;">
            <li style="margin-left: 2em;">3 for winning a match</li>
            <li style="margin-left: 2em; margin-top: .25em;">2 for drawing a match</li>
            <li style="margin-left: 2em; margin-top: .25em;">1 for playing a match</li>
            <li style="margin-left: 2em; margin-top: .25em;">0.1 for each game won in a match</li>
            <li style="margin-left: 2em; margin-top: .25em;">total score for each match is then divided by the division</li>
            <li style="margin-left: 2em; margin-top: .25em;">divisions for the next round are allocated:
                <ul style="list-style-type: circle;">
                    <li style="margin-left: 2em; margin-top: .25em;">based on points for the previous two rounds (i.e. players with the highest points are allocated to division 1 )</li>
                    <li style="margin-left: 2em; margin-top: .25em;">players with zero points are allocated alphabetically below all other players</li>
                </ul>
            </li>
        </ul>
        <p style="margin: 0.25em 0;">To view how your individual points are calculated for each match see your <a href="/account#matches">Account</a> page.</p>
    </div>
    <#else>
    <div id="no_rounds" class="errors_messages" style="margin: 5em auto;">
        <p style="margin: 0.25em 0;">There are currently no rounds for this club...</p>
    </div>
    </#if>
</#macro>

<@page_html/>