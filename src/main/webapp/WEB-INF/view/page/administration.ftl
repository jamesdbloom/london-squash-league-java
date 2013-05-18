<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Leagues</title>
</#macro>

<#macro content_header>
<div id="header">Leagues</div>
</#macro>

<#macro content_section>
<h2 class="table_title">Clubs</h2>

<form method="post" action="/club">
    <@errors.print_errors "club"/>
    <table class="action_table">
        <tbody>
            <tr>
                <th class="key">Id</th>
                <th class="club">Name</th>
                <th class="address">Address</th>
                <th class="button_column last"></th>
            </tr>
            <#list clubs as club>
                <tr>
                    <td class="key">${club.id}</td>
                    <td class="name">${club.name}</td>
                    <td class="address">${club.address}</td>
                    <td class="button_column last"><a class="button" href="/club/delete/${club.id}">Delete</a><a class="button" href="/club/update/${club.id}">Modify</a></td>
                </tr>
            </#list>
            <tr class="create_row">
                <td class="key last"></td>
                <td class="name last"><@spring.formInput  path="club.name" attributes="pattern='.{3,25}' class='show_validation'"/><@spring.showErrors "<br>" /></td>
                <td class="address last"><@spring.formInput  path="club.address" attributes="pattern='.{3,25}' class='show_validation'"/><@spring.showErrors "<br>" /></td>
                <td class="button_column last"><input type="submit" name="save" value="save"></td>
            </tr>
        </tbody>
    </table>

</form>

<h2 class="table_title">Leagues</h2>

<form method="post" action="/league">
    <@errors.print_errors "league"/>
    <table class="action_table">
        <tbody>
            <tr>
                <th class="key">Id</th>
                <th class="club">Club</th>
                <th class="name">League</th>
                <th class="button_column last"></th>
            </tr>
            <#list leagues as league>
                <tr>
                    <td class="key">${league.id}</td>
                    <td class="club">${league.club.name}</td>
                    <td class="name">${league.name}</td>
                    <td class="button_column last"><a class="button" href="/league/delete/${league.id}">Delete</a><a class="button" href="/league/update/${league.id}">Modify</a></td>
                </tr>
            </#list>
            <tr class="create_row">
                <td class="key last"></td>
                <td class="club last">
                    <#if (clubs?size > 0)>
                        <select name="club">
                            <#list clubs as club>
                                <option value="${club.id}">${club.name}</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="name last"><@spring.formInput  path="league.name" attributes="pattern='.{3,25}' class='show_validation'"/><@spring.showErrors "<br>" /></td>
                <td class="button_column last"><input type="submit" name="save" value="save"></td>
            </tr>
        </tbody>
    </table>
</form>

<h2 class="table_title">Divisions</h2>

<form method="post" action="/division">
    <@errors.print_errors "division"/>
    <table class="action_table">
        <tbody>
            <tr>
                <th class="key">Id</th>
                <th class="division">League</th>
                <th class="name">Division</th>
                <th class="button_column last"></th>
            </tr>
            <#list divisions as division>
                <tr>
                    <td class="key">${division.id}</td>
                    <td class="division">${division.league.club.name} &ndash; ${division.league.name}</td>
                    <td class="name">${division.name}</td>
                    <td class="button_column last"><a class="button" href="/division/delete/${division.id}">Delete</a><a class="button" href="/division/update/${division.id}">Modify</a></td>
                </tr>
            </#list>
            <tr class="create_row">
                <td class="key last"></td>
                <td class="club last">
                    <#if (leagues?size > 0)>
                        <select name="league">
                            <#list leagues as league>
                                <option value="${league.id}">${league.club.name} &ndash; ${league.name}</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="name last"><@spring.formInput  path="division.name" attributes="pattern='.{3,25}' class='show_validation'"/><@spring.showErrors "<br>" /></td>
                <td class="button_column last"><input type="submit" name="save" value="save"></td>
            </tr>
        </tbody>
    </table>
</form>

<h2 class="table_title">Rounds</h2>

<form method="post" action="/round">
    <@errors.print_errors "round"/>
    <table class="action_table">
        <tbody>
            <tr>
                <th class="key">Id</th>
                <th class="division">Division</th>
                <th class="status hide_on_small_screen">Status</th>
                <th class="date">Start</th>
                <th class="date">End</th>
                <th class="button_column last"></th>
            </tr>
            <#list rounds as round>
                <tr>
                    <td class="key">${round.id}</td>
                    <td class="division">${round.division.league.club.name} &ndash; ${round.division.league.name} &ndash; ${round.division.name}</td>
                    <td class="status hide_on_small_screen">${round.status}</td>
                    <td class="date">${round.startDate?date("yyyy-MM-dd")?string("dd MMM yyyy")}</td>
                    <td class="date">${round.endDate?date("yyyy-MM-dd")?string("dd MMM yyyy")}</td>
                    <td class="button_column last"><a class="button" href="/round/delete/${round.id}">Delete</a><a class="button" href="/round/update/${round.id}">Modify</a></td>
                </tr>
            </#list>
            <tr class="create_row">
                <td class="key last"></td>
                <td class="division last">
                    <#if (divisions?size > 0)>
                        <select name="division">
                            <#list divisions as division>
                                <option value="${division.id}">${division.league.club.name} &ndash; ${division.league.name} &ndash; ${division.name}</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="status hide_on_small_screen last"></td>
                <td class="date last"><@spring.formInput  path="round.startDate" fieldType="date"/><@spring.showErrors "<br>" /></td>
                <td class="date last"><@spring.formInput  path="round.endDate" fieldType="date"/><@spring.showErrors "<br>" /></td>
                <td class="button_column last"><input type="submit" name="save" value="save"></td>
            </tr>
        </tbody>
    </table>
</form>

<h2 class="table_title">Users</h2>

<table class="action_table">
    <tbody>
        <tr>
            <th class="key">Id</th>
            <th class="name">User</th>
            <th class="email">Email</th>
            <th class="mobile">Mobile</th>
            <th class="button_column last"></th>
        </tr>
        <#list users as user>
            <tr>
                <td class="key">${user.id}</td>
                <td class="name">${user.name}</td>
                <td class="email">${user.email}</td>
                <td class="mobile">${user.mobile}</td>
                <td class="button_column last"><a class="button" href="/user/delete/${user.id}">Delete</a><a class="button" href="/user/update/${user.id}">Modify</a></td>
            </tr>
        </#list>
    </tbody>
</table>

<h2 class="table_title">Players</h2>

<form method="post" action="/player">
    <@errors.print_errors "player"/>
    <table class="action_table">
        <tbody>
            <tr>
                <th class="key">Id</th>
                <th class="division">Division</th>
                <th class="name">User</th>
                <th class="status">Status</th>
                <th class="button_column last"></th>
            </tr>
            <#list players as player>
                <tr>
                    <td class="key">${player.id}</td>
                    <td class="division">${player.currentDivision.league.club.name} &ndash; ${player.currentDivision.league.name} &ndash; ${player.currentDivision.name}</td>
                    <td class="name">${player.user.name}</td>
                    <td class="status hide_on_small_screen">${player.status}</td>
                    <td class="button_column last"><a class="button" href="/player/delete/${player.id}">Delete</a><a class="button" href="/player/update/${player.id}">Modify</a></td>
                </tr>
            </#list>
            <tr class="create_row">
                <td class="key last"></td>
                <td class="division last">
                    <#if (divisions?size > 0)>
                        <select name="currentDivision">
                            <#list divisions as division>
                                <option value="${division.id}">${division.league.club.name} &ndash; ${division.league.name} &ndash; ${division.name}</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="name last">
                    <#if (divisions?size > 0)>
                        <select name="user">
                            <#list users as user>
                                <option value="${user.id}">${user.name}</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="status hide_on_small_screen last">
                    <select name="status">
                        <#list playerStatuses as playerStatus>
                            <option value="${playerStatus}">${playerStatus}</option>
                        </#list>
                    </select>
                </td>
                <td class="button_column last"><input type="submit" name="save" value="save"></td>
            </tr>
        </tbody>
    </table>
</form>

<p class="num_of_players">Number of Players: ${players?size}</p>

<h2 class="table_title">Matches</h2>

<form method="post" action="/match">
    <@errors.print_errors "match"/>
    <table class="action_table">
        <tbody>
            <tr>
                <th class="key">Id</th>
                <th class="round">Round</th>
                <th class="player">Player One</th>
                <th class="player">Player Two</th>
                <th class="score">Score</th>
                <th class="score_entered hide_on_medium_screen">Score Entered</th>
                <th class="button_column last"></th>
            </tr>
            <#list matches as match>
                <tr>
                    <td class="key">${match.id}</td>
                    <td class="round">${match.round.division.league.club.name} &ndash; ${match.round.division.league.name} &ndash; ${match.round.division.name}</td>
                    <td class="player">${match.playerOne.user.name}</td>
                    <td class="player">${match.playerTwo.user.name}</td>
                    <td class="score">${match.score}</td>
                    <td class="score_entered">${match.scoreEntered?date("yyyy-MM-dd")?string("dd MMM yyyy")}</td>
                    <td class="button_column last"><a class="button" href="/match/delete/${match.id}">Delete</a><a class="button" href="/match/update/${match.id}">Modify</a></td>
                </tr>
            </#list>
            <tr class="create_row">
                <td class="key last"></td>
                <td class="round last">
                    <#if (rounds?size > 0)>
                        <select name="round">
                            <#list rounds as round>
                                <option value="${round.id}">${round.division.league.club.name} &ndash; ${round.division.league.name} &ndash; ${round.division.name}</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="player last">
                    <#if (players?size > 0)>
                        <select name="playerOne">
                            <#list players as player>
                                <option value="${player.id}">${player.user.name}</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="player last">
                    <#if (players?size > 0)>
                        <select name="playerTwo">
                            <#list players as player>
                                <option value="${player.id}">${player.user.name}</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="score last"><@spring.formInput  path="match.score" attributes="pattern='.{3,25}' class='show_validation'"/><@spring.showErrors "<br>" /></td>
                <td class="score_entered last"></td>
                <td class="button_column last"><input type="submit" name="save" value="save"></td>
            </tr>
        </tbody>
    </table>
</form>
</#macro>

<@page_html/>