<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Leagues</title>
</#macro>

<#macro content_header>
<div id="header">Leagues</div>
</#macro>

<#assign please_select = "Please select" />

<#macro content_section>
<h2 id="roles" class="table_title">Roles</h2>

<form method="post" action="/role/save">
    <@errors.print_errors "role"/>
    <table class="action_table">
        <tbody>
            <tr>
                <th class="key">Id</th>
                <th class="name">Name</th>
                <th class="description">Description</th>
                <th class="club">Club</th>
                <th class="button_column last"></th>
            </tr>
            <#list roles as role>
                <tr>
                    <td class="key">${role.id}</td>
                    <td class="name">${role.name}</td>
                    <td class="description">${role.description}</td>
                    <td class="club"><#if role.club??>${role.club.name}</#if></td>
                    <td class="button_column last"><a class="button" href="/role/delete/${role.id}">Delete</a><a class="button" href="/role/update/${role.id}">Modify</a></td>
                </tr>
            </#list>
            <tr class="create_row" id="create_role">
                <td class="key last"></td>
                <td class="name last"><@spring.formInput  path="role.name" attributes="required='required' pattern='.{3,25}' title='${environment.getProperty(\"validation.role.name\")}' class='show_validation'"/></td>
                <td class="description last"><@spring.formInput  path="role.description" attributes="required='required' pattern='.{3,25}' title='${environment.getProperty(\"validation.role.description\")}' class='show_validation'"/></td>
                <td class="club last">
                    <#if (clubs?size > 0)>
                        <select id="club" name="club">
                            <option value="">${please_select}</option>
                            <#list clubs as club>
                                <option value="${club.id}" <#if role.club?? && club.id == role.club.id>selected="selected"</#if>>${club.name}</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="button_column last"><input type="submit" name="save" value="save"></td>
            </tr>
        </tbody>
    </table>
</form>


<h2 id="users" class="table_title">Users</h2>

<form method="post" action="/user/save">
    <@errors.print_errors "user"/>
    <table class="action_table">
        <tbody>
            <tr>
                <th class="key">Id</th>
                <th class="name">User</th>
                <th class="email">Email</th>
                <th class="mobile">Mobile</th>
                <th class="mobile">Mobile Private</th>
                <th class="mobile">Role</th>
                <th class="button_column last"></th>
            </tr>
            <#list users as user>
                <tr>
                    <td class="key">${user.id}</td>
                    <td class="name">${user.name}</td>
                    <td class="email">${user.email}</td>
                    <td class="mobile">${user.mobile}</td>
                    <td class="mobilePrivacy">${user.mobilePrivacy}</td>
                    <td class="roles"><#list user.roles as role>${role.name}<#if role_has_next>, </#if></#list></td>
                    <td class="button_column last"><a class="button" href="/user/delete/${user.id}">Delete</a><a class="button" href="/user/update/${user.id}">Modify</a></td>
                </tr>
            </#list>
            <tr class="create_row" id="create_user">
                <td class="key last"></td>
                <td class="name last"><@spring.formInput  path="user.name" attributes="required='required' pattern='.{3,25}' title='${environment.getProperty(\"validation.user.name\")}' class='show_validation'"/></td>
                <td class="email last"><@spring.formInput  path="user.email" fieldType="email" attributes="required='required' title='${environment.getProperty(\"validation.user.email\")}' class='show_validation'"/></td>
                <td class="mobile last"><@spring.formInput  path="user.mobile" attributes="required='required' pattern='[\\d\\s]{6,15}' title='${environment.getProperty(\"validation.user.mobile\")}' class='show_validation'"/></td>
                <td class="status hide_on_small_screen last">
                    <@spring.bind "mobilePrivacyOptions" />
                    <@spring.formSingleSelectWithEmpty path="user.mobilePrivacy" options=mobilePrivacyOptions emptyValueMessage="${please_select}" />
                </td>
                <td class="roles hide_on_small_screen last">
                    <#if (clubs?size > 0)>
                        <select id="roles" name="roles" multiple="multiple" required='required'>
                            <option value="">${please_select}</option>
                            <#list roles as role>
                                <option value="${role.id}" <#if user.hasRole(role) >selected="selected"</#if>>${role.description}</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="button_column last"><input type="submit" name="save" value="save"></td>
            </tr>
        </tbody>
    </table>
</form>

<h2 id="clubs" class="table_title">Clubs</h2>

<form method="post" action="/club/save">
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
            <tr class="create_row" id="create_club">
                <td class="key last"></td>
                <td class="name last"><@spring.formInput  path="club.name" attributes="required='required' pattern='.{5,25}' title='${environment.getProperty(\"validation.club.name\")}' class='show_validation'"/></td>
                <td class="address last"><@spring.formInput  path="club.address" attributes="required='required' pattern='.{5,50}' title='${environment.getProperty(\"validation.club.address\")}' class='show_validation'"/></td>
                <td class="button_column last"><input type="submit" name="save" value="save"></td>
            </tr>
        </tbody>
    </table>

</form>

<h2 id="leagues" class="table_title">Leagues</h2>

<form method="post" action="/league/save">
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
            <tr class="create_row" id="create_league">
                <td class="key last"></td>
                <td class="club last">
                    <#if (clubs?size > 0)>
                        <select id="club" name="club" required='required' title='${environment.getProperty("validation.league.club")}'>
                            <option value="">${please_select}</option>
                            <#list clubs as club>
                                <option value="${club.id}" <#if league.club?? && club.id == league.club.id>selected="selected"</#if>>${club.name}</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="name last"><@spring.formInput  path="league.name" attributes="required='required' pattern='.{5,25}' title='${environment.getProperty(\"validation.league.name\")}' class='show_validation'"/></td>
                <td class="button_column last"><input type="submit" name="save" value="save"></td>
            </tr>
        </tbody>
    </table>
</form>

<h2 id="divisions" class="table_title">Divisions</h2>

<form method="post" action="/division/save">
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
            <tr class="create_row" id="create_division">
                <td class="key last"></td>
                <td class="club last">
                    <#if (leagues?size > 0)>
                        <select id="league" name="league" required='required' title='${environment.getProperty("validation.division.league")}'>
                            <option value="">${please_select}</option>
                            <#list leagues as league>
                                <option value="${league.id}" <#if division.league?? && league.id == division.league.id>selected="selected"</#if>>${league.club.name} &ndash; ${league.name}</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="name last"><@spring.formInput  path="division.name" attributes="required='required' pattern='.{5,25}' title='${environment.getProperty(\"validation.division.name\")}' class='show_validation'"/></td>
                <td class="button_column last"><input type="submit" name="save" value="save"></td>
            </tr>
        </tbody>
    </table>
</form>

<h2 id="rounds" class="table_title">Rounds</h2>

<form method="post" action="/round/save">
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
            <tr class="create_row" id="create_round">
                <td class="key last"></td>
                <td class="division last">
                    <#if (divisions?size > 0)>
                        <select id="division" name="division" required='required' title='${environment.getProperty("validation.round.division")}'>
                            <option value="">${please_select}</option>
                            <#list divisions as division>
                                <option value="${division.id}" <#if round.division?? && division.id == round.division.id>selected="selected"</#if>>${division.league.club.name} &ndash; ${division.league.name} &ndash; ${division.name}</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="status hide_on_small_screen last"></td>
                <td class="date last"><@spring.formInput  path="round.startDate" fieldType="date" attributes="required='required' title='${environment.getProperty(\"validation.round.startDate\")}'"/></td>
                <td class="date last"><@spring.formInput  path="round.endDate" fieldType="date" attributes="required='required' title='${environment.getProperty(\"validation.round.endDate\")}'"/></td>
                <td class="button_column last"><input type="submit" name="save" value="save"></td>
            </tr>
        </tbody>
    </table>
</form>

<h2 id="players" class="table_title">Players</h2>

<form method="post" action="/player/save">
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
            <tr class="create_row" id="create_player">
                <td class="key last"></td>
                <td class="division last">
                    <#if (divisions?size > 0)>
                        <select id="currentDivision" name="currentDivision" required='required' title='${environment.getProperty("validation.player.currentDivision")}'>
                            <option value="">${please_select}</option>
                            <#list divisions as division>
                                <option value="${division.id}" <#if player.currentDivision?? && division.id == player.currentDivision.id>selected="selected"</#if>>${division.league.club.name} &ndash; ${division.league.name} &ndash; ${division.name}</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="name last">
                    <#if (divisions?size > 0)>
                        <select id="user" name="user" required='required' title='${environment.getProperty("validation.player.user")}'>
                            <option value="">${please_select}</option>
                            <#list users as user>
                                <option value="${user.id}" <#if player.user?? && user.id == player.user.id>selected="selected"</#if>>${user.name}</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="status hide_on_small_screen last" required='required' title='${environment.getProperty("validation.player.status")}'>
                    <@spring.bind "playerStatuses" />
                    <@spring.formSingleSelectWithEmpty path="player.status" options=playerStatuses emptyValueMessage="${please_select}" />
                </td>
                <td class="button_column last"><input type="submit" name="save" value="save"></td>
            </tr>
        </tbody>
    </table>
</form>

<p class="num_of_players">Number of Players: ${players?size}</p>

<h2 id="matches" class="table_title">Matches</h2>

<form method="post" action="/match/save">
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
                    <td class="score">${match.score!""}</td>
                    <td class="score_entered"><#if match.scoreEntered??>${match.scoreEntered?date("yyyy-MM-dd")?string("dd MMM yyyy")}</#if></td>
                    <td class="button_column last"><a class="button" href="/match/delete/${match.id}">Delete</a><a class="button" href="/match/update/${match.id}">Modify</a></td>
                </tr>
            </#list>
            <tr class="create_row" id="create_match">
                <td class="key last"></td>
                <td class="round last">
                    <#if (rounds?size > 0)>
                        <select id="round" name="round" required='required' title='${environment.getProperty("validation.match.round")}'>
                            <option value="">${please_select}</option>
                            <#list rounds as round>
                                <option value="${round.id}" <#if match.round?? && round.id == match.round.id>selected="selected"</#if>>${round.division.league.club.name} &ndash; ${round.division.league.name} &ndash; ${round.division.name} &ndash; (${round.startDate?date("yyyy-MM-dd")?string("dd MMM yyyy")} &ndash; ${round.endDate?date("MM-dd")?string("dd MMM yyyy")})</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="player last">
                    <#if (players?size > 0)>
                        <select id="playerOne" name="playerOne" required='required' title='${environment.getProperty("validation.match.playerOne")}'>
                            <option value="">${please_select}</option>
                            <#list players as player>
                                <option value="${player.id}" <#if match.playerOne?? && player.id == match.playerOne.id>selected="selected"</#if>>${player.user.name}</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="player last">
                    <#if (players?size > 0)>
                        <select id="playerTwo" name="playerTwo" required='required' title='${environment.getProperty("validation.match.playerTwo")}'>
                            <option value="">${please_select}</option>
                            <#list players as player>
                                <option value="${player.id}" <#if match.playerTwo?? && player.id == match.playerTwo.id>selected="selected"</#if>>${player.user.name}</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="score last"><@spring.formInput  path="match.score" attributes="pattern='[0-9]{1,2}-[0-9]{1,2}' class='show_validation'"/></td>
                <td class="score_entered last"><@spring.formInput  path="match.scoreEntered" fieldType="date"/></td>
                <td class="button_column last"><input type="submit" name="save" value="save"></td>
            </tr>
        </tbody>
    </table>
</form>
</#macro>

<@page_html/>