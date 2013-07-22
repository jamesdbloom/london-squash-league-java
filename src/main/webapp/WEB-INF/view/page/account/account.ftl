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
    <tbody class="strip_rows">
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
        <tbody class="strip_rows">
            <tr>
                <th>Club</th>
                <th><label for="unregisteredLeagues">League</label></th>
                <th>Division</th>
                <th class="hide_on_very_small_screen">Status</th>
                <th class="button_column last"></th>
            </tr>
            <#list user.players as player>
                <tr>
                    <td id="player_${player_index}_club">${player.league.club.name}</td>
                    <td id="player_${player_index}_league">${player.league.name}</td>
                    <td id="player_${player_index}_currentDivision"><#if player.currentDivision?? >${player.currentDivision.name}</#if></td>
                    <td id="player_${player_index}_status" class="hide_on_very_small_screen">${player.status}</td>
                    <td id="player_${player_index}_register" class="button_column last">
                        <#if player.status == 'ACTIVE'><a class="button" href="/account/unregister?player=${player.id}">Unregister</a><#else><a class="button" href="/account/register?player=${player.id}">Register</a></#if>
                    </td>
                </tr>
            </#list>
            <#if (unregisteredLeagues?size > 0)>
                <tr>
                    <td class="last" colspan="3">
                        <select id="unregisteredLeagues" name="league" required="required">
                            <option value="">${environment.getProperty("message.general.please_select_a_league")}</option>
                            <#list unregisteredLeagues as league>
                                <option value="${league.id}">${league.club.name}  &ndash; ${league.name}</option>
                            </#list>
                        </select>
                    </td>
                    <td class="last hide_on_very_small_screen"></td>
                    <td class="button_column last"><input <#if (user.players?size = 0) >class="submit primary"</#if> type="submit" name="register" value="Register"></td>
                </tr>
            </#if>
    </table>
</form>
    <#if (user.players?size = 0) >
    <p class="errors_messages">You are not registered with any leagues, please select a league from the drop-down and click the Register button.</p>
    <#elseif (rounds?size = 0) >
    <h2 class="table_title" style="margin-top: 1em;">Rounds</h2>
    <div class="errors_messages">
        <p>You are not currently in any rounds for one of the following reasons:</p>
        <ul style="margin: 1em; list-style: disc outside none;">
            <li style="margin-left: 2em;">you are not an active player in any of your leagues,</li>
            <li style="margin-left: 2em; margin-top: .25em;">you have recently become an active player in a league but a new round has not started yet.</li>
            <li style="margin-left: 2em; margin-top: .25em;">both of the above reasons</li>
        </ul>
    </div>
    <#else>
    <h2 class="table_title">Rounds</h2>
    <table>
        <tbody class="strip_rows">
            <tr>
                <th>Division</th>
                <th class="hide_on_small_screen">Status</th>
                <th>Start</th>
                <th>End</th>
            </tr>
            <#list rounds as round>
                <tr>
                    <td id="round_${round_index}_division">${round.league.club.name} &ndash; ${round.league.name}</td>
                    <td id="round_${round_index}_status" class="hide_on_small_screen">${round.status}</td>
                    <td id="round_${round_index}_startDate">${round.startDate.toDate()?string("dd MMM yyyy")}</td>
                    <td id="round_${round_index}_endDate">${round.endDate.toDate()?string("dd MMM yyyy")}</td>
                </tr>
            </#list>
        </tbody>
    </table><h2 class="table_title" id="matches">Your Matches</h2>
        <#assign totalPoints = 0/>
        <#assign totalNumberOfMatches = 0/>
        <#list user.players as player>
            <#if (player.matches?size > 0) && player.currentDivision??>
                <#assign totalNumberOfMatches = totalNumberOfMatches + player.matches?size/>
            <h2 class="table_subtitle">${player.currentDivision.round.league.club.name} &ndash; ${player.currentDivision.round.league.name}</h2>
            <table>
                <tbody class="strip_rows">
                    <tr>
                        <th class="hide_on_very_small_screen">Division</th>
                        <th>Round</th>
                        <th>Player One</th>
                        <th>Player Two</th>
                        <th class="hide_on_medium_screen">Score Entered</th>
                        <th>Score</th>
                        <th class="hide_on_very_small_screen">Points</th>
                    </tr>
                    <#list player.matches as match>
                        <#if match.playerOne.user.id == user.id>
                            <#assign totalPoints = totalPoints + match.playerOneTotalPoints/>
                        <#else>
                            <#assign totalPoints = totalPoints + match.playerTwoTotalPoints/>
                        </#if>
                        <tr>
                            <td id="match_${player_index}_${match_index}_division" class="hide_on_very_small_screen">${match.division.name}</td>
                            <td id="match_${player_index}_${match_index}_date">${match.division.round.startDate.toDate()?string("dd MMM yyyy")} &ndash; ${match.division.round.endDate.toDate()?string("dd MMM yyyy")}</td>
                            <td id="match_${player_index}_${match_index}_playerOne"><@showContactDetails match.playerOne.user user/></td>
                            <td id="match_${player_index}_${match_index}_playerTwo"><@showContactDetails match.playerTwo.user user/></td>
                            <td id="match_${player_index}_${match_index}_scoreEntered" class="hide_on_medium_screen"><#if match.scoreEntered??>${match.scoreEntered.toDate()?string("dd MMM yyyy")}</#if></td>
                            <td id="match_${player_index}_${match_index}_score" style="white-space: nowrap"><#if match.score?? >${match.score}<#else><a href="/score/${match.id}">enter</a></#if></td>
                            <td id="match_${player_index}_${match_index}_score" style="white-space: nowrap " class="hide_on_very_small_screen"><@showPoints match user/></td>
                        </tr>
                    </#list>
                    <tr>
                        <td class="last" colspan="7" style="text-align: right">Total:&nbsp;&nbsp;${totalPoints}</td>
                    </tr>
                </tbody>
            </table>
            <div class="standalone_link">
                <a id="mailto_${player_index}" href="mailto:<#list player.allOpponentsEmails as email>${email}<#if email_has_next>,</#if></#list>" target="_blank">email all ${player.currentDivision.round.league.name} &ndash; ${player.currentDivision.name} opponents</a>
            </div>
            <div class="errors_messages" style="margin: 5em auto;">
                <p style="margin: 0.25em 0;">The points are calculated as follows:</p>
                <ul style="margin: 1em; list-style-image: none; list-style-position: outside; list-style-type: disc;">
                    <li style="margin-left: 2em;">3 for winning a match</li>
                    <li style="margin-left: 2em; margin-top: .25em;">2 for drawing a match</li>
                    <li style="margin-left: 2em; margin-top: .25em;">1 for playing a match</li>
                    <li style="margin-left: 2em; margin-top: .25em;">0.1 for each game won in a match</li>
                    <li style="margin-left: 2em; margin-top: .25em;">total score for each match is then divided by the division</li>
                    <li style="margin-left: 2em; margin-top: .25em;">division for the next round is then based on ranking for the previous two rounds</li>
                </ul>
                <p style="margin: 0.25em 0;">To view how your individual points are calculated for each match see your <a href="/account#matches">Account</a> page.</p>
            </div>
            </#if>
        </#list>
        <#if totalNumberOfMatches == 0>
        <div class="errors_messages">
            <p>You currently have no matches for one of the following reasons:</p>
            <ul style="margin: 1em; list-style: disc outside none;">
                <li style="margin-left: 2em;">you have recently become an active player in a league but have no matches because a new round has not started yet.</li>
                <li style="margin-left: 2em; margin-top: .25em;">you have recently become an active player in a league but have no current division because a new round has not started yet.</li>
                <li style="margin-left: 2em; margin-top: .25em;">you are not an active player in any of your leagues,</li>
            </ul>
        </div>
        </#if>
    </#if>
</#macro>

<#macro showContactDetails user you>
    <#if user.id != you.id>
    ${user.name}<br>
        <#if user.showMobileToOpponent()><a href="tel:${user.mobile}">${user.mobile}</a><br></#if><a href="mailto:${user.email}" target="_blank"><span class="hide_on_small_screen">${user.email}</span><span class="display_on_small_screen">email</span></a>
    <#else>
    You
    </#if>
</#macro>

<#macro showPoints match you>
    <#if match.score?? >
        <#if match.playerOne.user.id == you.id>
        <table>
            <tbody>
                <tr>
                    <td>(&nbsp;${match.playerOneWonOrLostPoints}</td>
                    <td>+</td>
                    <td>${match.playerOneGamesPoints?string("0.0#")}&nbsp;)</td>
                    <td rowspan="2">=</td>
                    <td rowspan="2">${match.playerOneTotalPoints?string("0.00")}</td>
                </tr>
                <tr>
                    <td class="top_border top_border_right"></td>
                    <td class="top_border top_border_across">${match.division.name}</td>
                    <td class="top_border top_border_left"></td>
                </tr>
            </tbody>
        </table>
        <#else>
        <table>
            <tbody>
                <tr>
                    <td>(&nbsp;${match.playerTwoWonOrLostPoints}</td>
                    <td>+</td>
                    <td>${match.playerTwoGamesPoints?string("0.0#")}&nbsp;)</td>
                    <td rowspan="2">=</td>
                    <td rowspan="2">${match.playerTwoTotalPoints?string("0.00")}</td>
                </tr>
                <tr>
                    <td class="top_border top_border_right"></td>
                    <td class="top_border top_border_across">${match.division.name}</td>
                    <td class="top_border top_border_left"></td>
                </tr>
            </tbody>
        </table>
        </#if>
    </#if>
</#macro>

<@page_html/>