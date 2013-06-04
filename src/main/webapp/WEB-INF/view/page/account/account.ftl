<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Account</title>
</#macro>

<#macro content_header>
<div id="header">Account</div>
</#macro>

<#macro content_section>
<h2 class="table_title">Your Details</h2>
<table>
    <tbody>
        <tr>
            <th>Name</th>
            <th>Email</th>
            <th class="row_end_before_hidden_small_screen">Mobile</th>
            <th class="hide_on_small_screen">Mobile Privacy</th>
        </tr>
        <tr>
            <td id="user_name">${user.name}</td>
            <td id="user_email">${user.email}</td>
            <td id="user_mobile" class="row_end_before_hidden_small_screen">${user.mobile!""}</td>
            <td id="user_mobilePrivacy" class="hide_on_small_screen">${user.mobilePrivacy!""}</td>
        </tr>
    </tbody>
</table>
<div class="standalone_link"><a href="/account/updatePassword" title="Update Password">Update Password</a></div>
<div class="standalone_link"><a href="/user/update/${user.id}" title="Update User">Update User</a></div><h2 class="table_title" id="leagues">Leagues</h2>
<form method="post" action="/account/register">
    <@errors.print_errors_list "player"/>
    <table class="action_table">
        <tbody>
            <tr>
                <th>Club</th>
                <th><label for="league">League</label></th>
                <th class="hide_on_very_small_screen">Status</th>
                <th>Division</th>
                <th class="button_column last"></th>
            </tr>
            <#list user.players as player>
                <tr>
                    <td>${player.league.club.name}</td>
                    <td>${player.league.club.name} &ndash; ${player.league.name}</td>
                    <td class="hide_on_very_small_screen">${player.status}</td>
                    <td><#if player.currentDivision?? >${player.currentDivision.name}</#if></td>
                    <td class="button_column last">
                        <#if player.status == 'ACTIVE'><a class="button" href="/account/unregister?player=${player.id}">Unregister</a><#else><a class="button" href="/account/register?player=${player.id}">Register</a></#if>
                    </td>
                </tr>
            </#list>
            <#if (unregisteredLeagues?size > 0)>
                <tr>
                    <td class="last" colspan="4">
                        <select id="league" name="league" required="required">
                            <option value="">${environment.getProperty("message.general.please_select")}</option>
                            <#list unregisteredLeagues as league>
                                <option value="${league.id}">${league.name}</option>
                            </#list>
                        </select>
                    </td>
                    <td class="button_column last"><input <#if (user.players?size = 0) >class="submit primary"</#if> type="submit" name="register" value="Register"></td>
                </tr>
            </#if>
    </table>
</form>
    <#if (user.players?size = 0) >
    <p class="errors_messages">You are not registered with any leagues, please select a league from the drop-down and click the register button.</p>
    <#elseif (rounds?size = 0) >
    <h2 class="table_title" style="margin-top: 1em;">Rounds</h2>
    <div class="errors_messages">
        <p>You are not currently in any rounds for one of the following reasons:</p>
        <ul style="margin: 1em; list-style-image: none; list-style-position: outside; list-style-type: disc;">
            <li style="margin-left: 2em;">you are not an active player in any of your leagues,</li>
            <li style="margin-left: 2em; margin-top: .25em;">you have recently become an active player in a league but a new round has not started yet.</li>
        </ul>
    </div>
    <#else>
    <h2 class="table_title">Rounds</h2>
    <table>
        <tbody>
            <tr>
                <th>Division</th>
                <th class="hide_on_small_screen">Status</th>
                <th>Start</th>
                <th>End</th>
            </tr>
            <#list rounds as round>
                <tr>
                    <td>${round.division.league.club.name} &ndash; ${round.division.league.name} &ndash; ${round.division.name}</td>
                    <td class="hide_on_small_screen">${round.status}</td>
                    <td>${round.startDate.toDate()?string("dd MMM yyyy")}</td>
                    <td>${round.endDate.toDate()?string("dd MMM yyyy")}</td>
                </tr>
            </#list>
        </tbody>
    </table><h2 class="table_title" id="matches">Your Matches</h2>
        <#list user.players as player>
            <#if (player.matches?size > 0)>
            <h2 class="table_subtitle">${player.currentDivision.league.club.name} &ndash; ${player.currentDivision.league.name}</h2>
            <table>
                <tbody>
                    <tr>
                        <th class="hide_on_very_small_screen">Division</th>
                        <th>Round</th>
                        <th>Player One</th>
                        <th>Player Two</th>
                        <th class="hide_on_medium_screen">Score Entered</th>
                        <th>Score</th>
                    </tr>
                    <#list player.matches as match>
                        <tr>
                            <td class="hide_on_very_small_screen">${match.round.division.league.name} &ndash; ${match.round.division.name}</td>
                            <td>${match.round.startDate.toDate()?string("dd MMM yyyy")} &ndash; ${match.round.endDate.toDate()?string("dd MMM yyyy")}</td>
                            <td><@showContactDetails match.playerOne.user/></td>
                            <td><@showContactDetails match.playerTwo.user/></td>
                            <td class="hide_on_medium_screen"><#if match.scoreEntered??>${match.scoreEntered.toDate()?string("dd MMM yyyy")}</#if></td>
                            <td style="white-space: nowrap"><#if match.score?? >${match.score}<#else><a href="/score/${match.id}">enter</a></#if></td>
                        </tr>
                    </#list>
                </tbody>
            </table>
            <div class="standalone_link">
                <a href="mailto:<#list player.allOpponentsEmails as email>${email}<#if email_has_next>, </#if></#list>" target="_blank">email all ${player.currentDivision.league.name} &ndash; ${player.currentDivision.name} opponents</a>
            </div>
            </#if>
        </#list>
    </#if>
</#macro>

<#macro showContactDetails user>
${user.name}<br>
    <#if user.showMobileToOpponent()><a href="tel:${user.mobile}">${user.mobile}</a></#if><br><a href="mailto:${user.email}" target="_blank"><span class="hide_on_small_screen">${user.email}</span><span class="display_on_small_screen">email</span></a>
</#macro>

<@page_html/>