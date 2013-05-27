<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Account</title>
</#macro>

<#macro content_header>
<div id="header">Account</div>
</#macro>

<#macro content_section>
<h2 class="table_title">Your Details</h2>
<table class="">
    <tbody>
        <tr>
            <th class="name">Name</th>
            <th class="email">Email</th>
            <th class="mobile row_end_before_hidden_small_screen">Mobile</th>
            <th class="mobile_privacy hide_on_small_screen">Mobile Privacy</th>
        </tr>
        <tr class="">
            <td class="name">${user.name}</td>
            <td class="email">${user.email}</td>
            <td class="mobile row_end_before_hidden_small_screen">${user.mobile!""}</td>
            <td class="mobile_privacy hide_on_small_screen">${user.mobilePrivacy!""}</td>
        </tr>
    </tbody>
</table>
<div class="standalone_link"><a href="/account/updatePassword" title="Update Password">Update Password</a></div>
<div class="standalone_link"><a href="/user/update/${user.id}" title="Update User">Update User</a></div><h2 class="table_title" id="divisions">Leagues</h2>
<table class="action_table">
    <tbody>
        <tr>
            <th>Club</th>
            <th>League</th>
            <th class="status hide_on_very_small_screen">Status</th>
            <th>Division</th>
            <th class="button_column last"></th>
        </tr>
        <#list user.players as player>
            <tr>
                <td>${player.league.club.name}</td>
                <td>${player.currentDivision.league.club.name} &ndash; ${player.currentDivision.league.name}</td>
                <td class="status hide_on_very_small_screen">${player.status}</td>
                <td>${player.currentDivision.name}</td>
                <td class="button_column last"><#if player.status == 'ACTIVE'><a class="submit" href="/account/unregister/${player.id}">Unregister</a><#else><a class="submit" href="/account/register/${player.id}">Register</a></#if></td>
            </tr>
        </#list>
</table><h2 class="table_title">Rounds</h2>
<table>
    <tbody>
        <tr>
            <th class="league">Division</th>
            <th class="league">Status</th>
            <th class="date">Start</th>
            <th class="date">End</th>
        </tr>
    </tbody>
    <#list user.players as player>
        <#list player.currentDivision.rounds as round>
            <tr>
                <td class="division">${round.division.league.club.name} &ndash; ${round.division.league.name} &ndash; ${round.division.name}</td>
                <td class="status hide_on_small_screen">${round.status}</td>
                <td class="date">${round.startDate.toDate()?string("dd MMM yyyy")}</td>
                <td class="date">${round.endDate.toDate()?string("dd MMM yyyy")}</td>
            </tr>
        </#list>
    </#list>
</table><h2 class="table_title" id="matches">Your Matches</h2>
    <#list user.players as player>
    <h3>${player.currentDivision.league.club.name} &ndash; ${player.currentDivision.league.name}</h3>
    <table>
        <tbody>
            <tr>
                <th class="division">Division</th>
                <th class="round hide_on_small_screen">Round</th>
                <th class="player">Player One</th>
                <th class="player">Player Two</th>
                <th class="score">Score</th>
                <th class="score">Score Entered</th>
            </tr>
            <#list playerToMatches?keys as key>
                <#if key = player.id>
                    <#list playerToMatches[key] as match>
                        <td class="round">${match.round.division.league.club.name} &ndash; ${match.round.division.league.name} &ndash; ${match.round.division.name}</td>
                        <td class="round">${round.startDate.toDate()?string("dd MMM yyyy")} &ndash; ${round.endDate.toDate()?string("dd MMM yyyy")}</td>
                        <td class="player">${match.playerOne.user.name}</td>
                        <td class="player">${match.playerTwo.user.name}</td>
                        <td class="score">${match.score!""}</td>
                        <td class="score_entered"><#if match.scoreEntered??>${match.scoreEntered.toDate()?string("dd MMM yyyy")}</#if></td>
                    </#list>
                </#if>
            </#list>
        </tbody>
    </table>
    </#list>
</#macro>

<@page_html/>