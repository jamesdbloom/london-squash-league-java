<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Leagues</title>
</#macro>

<#macro content_header>
<div id="header">Leagues</div>
</#macro>

<#macro content_section>
<h2 id="roles" class="table_title">Roles</h2>

<form method="post" action="/role/save">
    <@errors.print_binding_errors "role"/>
    <table class="action_table">
        <tbody class="strip_rows">
            <tr>
                <th class="hide_on_small_screen">Id</th>
                <th>Name</th>
                <th class="description">Description</th>
                <th class="club"><label for="club">Club</label></th>
                <th class="button_column last"></th>
            </tr>
            <#list roles as role>
                <tr>
                    <td class="hide_on_small_screen">${role.id}</td>
                    <td>${role.name}</td>
                    <td>${role.description}</td>
                    <td><#if role.club??>${role.club.name}</#if></td>
                    <td class="button_column last"><a class="button" href="/role/delete/${role.id}">Delete</a><a class="button" href="/role/update/${role.id}">Modify</a></td>
                </tr>
            </#list>
            <tr class="create_row" id="create_role">
                <td class="last hide_on_small_screen"></td>
                <td class="last"><@spring.formInput  path="role.name" attributes='required="required" pattern=".{5,25}" maxlength="25" title="${environment.getProperty("validation.role.name")}" class="show_validation"'/></td>
                <td class="last"><@spring.formInput  path="role.description" attributes='required="required" pattern=".{5,50}" maxlength="50" title="${environment.getProperty("validation.role.description")}" class="show_validation"'/></td>
                <td class="last">
                    <#if (clubs?size > 0)>
                        <select id="club" name="club">
                            <option value="">${environment.getProperty("message.general.please_select")}</option>
                            <#list clubs as club>
                                <option value="${club.id}" <#if role.club?? && club.id == role.club.id>selected="selected"</#if>>${club.name}</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="button_column last"><input type="submit" value="save"></td>
            </tr>
        </tbody>
    </table>
</form>

<h2 id="users" class="table_title">Users</h2>

<form method="post" action="/user/save">
    <@errors.print_binding_errors "user"/>
    <table class="action_table">
        <tbody class="strip_rows">
            <tr>
                <th class="hide_on_small_screen">Id</th>
                <th>User</th>
                <th>Email</th>
                <th>Mobile</th>
                <th class="hide_on_small_screen">Mobile Private</th>
                <th class="hide_on_very_small_screen"><label for="roles">Role</label></th>
                <th class="button_column last"></th>
            </tr>
            <#list users as user>
                <tr>
                    <td class="hide_on_small_screen">${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.mobile!""}</td>
                    <td class="hide_on_small_screen">${user.mobilePrivacy!""}</td>
                    <td class="hide_on_very_small_screen"><#list user.roles as role>${role.name}<#if role_has_next>, </#if></#list></td>
                    <td class="button_column last"><a class="button" href="/user/delete/${user.id}">Delete</a><a class="button" href="/user/update/${user.id}">Modify</a></td>
                </tr>
            </#list>
            <tr class="create_row" id="create_user">
                <td class="last hide_on_small_screen"></td>
                <td class="last"><@spring.formInput  path="user.name" attributes='required="required" pattern=".{3,50}" maxlength="50" title="${environment.getProperty("validation.user.name")}" class="show_validation"'/></td>
                <td class="last"><@spring.formInput  path="user.email" fieldType="email" attributes='required="required" maxlength="25" title="${environment.getProperty("validation.user.email")}" class="show_validation"'/></td>
                <td class="last"><@spring.formInput  path="user.mobile" attributes='required="required" pattern="[\\d\\s]{6,15}" maxlength="25" title="${environment.getProperty("validation.user.mobile")}" class="show_validation"'/></td>
                <td class="last hide_on_small_screen">
                    <@spring.bind "mobilePrivacyOptions" />
                    <@spring.formSingleSelectWithEmpty path="user.mobilePrivacy" options=mobilePrivacyOptions emptyValueMessage='${environment.getProperty("message.general.please_select")}' attributes='required="required" title="${environment.getProperty("validation.user.mobilePrivacy")}"' />
                </td>
                <td class="last hide_on_very_small_screen">
                    <#if (roles?size > 0)>
                        <select id="roles" name="roles" <#if (roles?size > 1)>multiple="multiple"</#if> required="required">
                            <#if (roles?size <= 1)>
                                <option value="">${environment.getProperty("message.general.please_select")}</option></#if>
                            <#list roles as role>
                                <option value="${role.name}" <#if user.hasRole(role) >selected="selected"</#if>>${role.description}</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="button_column last"><input type="submit" value="save"></td>
            </tr>
        </tbody>
    </table>
</form>

<h2 id="clubs" class="table_title">Clubs</h2>

<form method="post" action="/club/save">
    <@errors.print_binding_errors "club"/>
    <table class="action_table">
        <tbody class="strip_rows">
            <tr>
                <th class="hide_on_small_screen">Id</th>
                <th>Name</th>
                <th>Address</th>
                <th class="button_column last"></th>
            </tr>
            <#list clubs as club>
                <tr>
                    <td class="hide_on_small_screen">${club.id}</td>
                    <td>${club.name}</td>
                    <td>${club.address}</td>
                    <td class="button_column last"><a class="button" href="/club/delete/${club.id}">Delete</a><a class="button" href="/club/update/${club.id}">Modify</a></td>
                </tr>
            </#list>
            <tr class="create_row" id="create_club">
                <td class="last hide_on_small_screen"></td>
                <td class="last"><@spring.formInput  path="club.name" attributes='required="required" pattern=".{5,25}" maxlength="25" title="${environment.getProperty("validation.club.name")}" class="show_validation"'/></td>
                <td class="last"><@spring.formInput  path="club.address" attributes='required="required" pattern=".{5,50}" maxlength="50" title="${environment.getProperty("validation.club.address")}" class="show_validation"'/></td>
                <td class="button_column last"><input type="submit" value="save"></td>
            </tr>
        </tbody>
    </table>

</form>

<h2 id="leagues" class="table_title">Leagues</h2>

<form method="post" action="/league/save">
    <@errors.print_binding_errors "league"/>
    <table class="action_table">
        <tbody class="strip_rows">
            <tr>
                <th class="hide_on_small_screen">Id</th>
                <th>Club</th>
                <th>League</th>
                <th class="button_column last"></th>
            </tr>
            <#list leagues as league>
                <tr>
                    <td class="hide_on_small_screen">${league.id}</td>
                    <td>${league.club.name}</td>
                    <td>${league.name}</td>
                    <td class="button_column last"><a class="button" href="/league/delete/${league.id}">Delete</a><a class="button" href="/league/update/${league.id}">Modify</a></td>
                </tr>
            </#list>
            <tr class="create_row" id="create_league">
                <td class="last hide_on_small_screen"></td>
                <td class="last">
                    <#if (clubs?size > 0)>
                        <select id="club" name="club" required="required" title="${environment.getProperty("validation.league.club")}">
                            <option value="">${environment.getProperty("message.general.please_select")}</option>
                            <#list clubs as club>
                                <option value="${club.id}" <#if league.club?? && club.id == league.club.id>selected="selected"</#if>>${club.name}</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="last"><@spring.formInput  path="league.name" attributes='required="required" pattern=".{5,25}" maxlength="25" title="${environment.getProperty("validation.league.name")}" class="show_validation"'/></td>
                <td class="button_column last"><input type="submit" value="save"></td>
            </tr>
        </tbody>
    </table>
</form>

<h2 id="rounds" class="table_title">Rounds</h2>

<form method="post" action="/round/save">
    <@errors.print_binding_errors "round"/>
    <table class="action_table">
        <tbody class="strip_rows">
            <tr>
                <th class="hide_on_small_screen">Id</th>
                <th><label for="division">League</label></th>
                <th class="hide_on_small_screen">Status</th>
                <th>Start</th>
                <th>End</th>
                <th class="button_column last"></th>
            </tr>
            <#list rounds as round>
                <tr>
                    <td class="hide_on_small_screen">${round.id}</td>
                    <td>${round.league.club.name} &ndash; ${round.league.name}</td>
                    <td class="hide_on_small_screen">${round.status}</td>
                    <td>${round.startDate.toDate()?string("dd MMM yyyy")}</td>
                    <td>${round.endDate.toDate()?string("dd MMM yyyy")}</td>
                    <td class="button_column last"><a class="button" href="/round/delete/${round.id}">Delete</a><a class="button" href="/round/update/${round.id}">Modify</a></td>
                </tr>
            </#list>
            <tr class="create_row" id="create_round">
                <td class="last hide_on_small_screen"></td>
                <td class="last">
                    <#if (leagues?size > 0)>
                        <select id="league" name="league" required="required" title="${environment.getProperty("validation.round.league")}">
                            <option value="">${environment.getProperty("message.general.please_select")}</option>
                            <#list leagues as league>
                                <option value="${league.id}" <#if round.league?? && league.id == round.league.id>selected="selected"</#if>>${league.club.name} &ndash; ${league.name}</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="hide_on_small_screen last"></td>
                <td class="last"><@spring.formInput  path="round.startDate" fieldType="date" attributes='required="required" title="${environment.getProperty("validation.round.startDate")}"'/></td>
                <td class="last"><@spring.formInput  path="round.endDate" fieldType="date" attributes='required="required" title="${environment.getProperty("validation.round.endDate")}"'/></td>
                <td class="button_column last"><input type="submit" value="save"></td>
            </tr>
        </tbody>
    </table>
</form>

<h2 id="divisions" class="table_title">Divisions</h2>

<form method="post" action="/division/save">
    <@errors.print_binding_errors "division"/>
    <table class="action_table">
        <tbody class="strip_rows">
            <tr>
                <th class="hide_on_small_screen">Id</th>
                <th>League</th>
                <th><label for="league">Division</label></th>
                <th class="button_column last"></th>
            </tr>
            <#list divisions as division>
                <tr>
                    <td class="hide_on_small_screen">${division.id}</td>
                    <td>${division.round.league.club.name} &ndash; ${division.round.league.name} &ndash; (${division.round.startDate.toDate()?string("dd MMM yyyy")} &ndash; ${division.round.endDate.toDate()?string("dd MMM yyyy")})</td>
                    <td>${division.name}</td>
                    <td class="button_column last"><a class="button" href="/division/delete/${division.id}">Delete</a><a class="button" href="/division/update/${division.id}">Modify</a></td>
                </tr>
            </#list>
            <tr class="create_row" id="create_division">
                <td class="last hide_on_small_screen"></td>
                <td class="last">
                    <#if (rounds?size > 0)>
                        <select id="round" name="round" required="required" title="${environment.getProperty("validation.round.division")}">
                            <option value="">${environment.getProperty("message.general.please_select")}</option>
                            <#list rounds as round>
                                <option value="${round.id}" <#if division.round?? && round.id == division.round.id>selected="selected"</#if>>${round.league.club.name} &ndash; ${round.league.name} &ndash; (${round.startDate.toDate()?string("dd MMM yyyy")} &ndash; ${round.endDate.toDate()?string("dd MMM yyyy")})</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="last"><@spring.formInput  path="division.name" fieldType="number" attributes='required="required" min="0" maxlength="25" title="${environment.getProperty("validation.division.name")}" class="show_validation"'/></td>
                <td class="button_column last"><input type="submit" value="save"></td>
            </tr>
        </tbody>
    </table>
</form>

<h2 id="players" class="table_title">Players</h2>

<form method="post" action="/player/save">
    <@errors.print_binding_errors "player"/>
    <table class="action_table">
        <tbody class="strip_rows">
            <tr>
                <th class="hide_on_small_screen">Id</th>
                <th><label for="currentDivision">Division / League</label></th>
                <th><label for="user">User</label></th>
                <th class="hide_on_small_screen status">Status</th>
                <th class="button_column last"></th>
            </tr>
            <#list players as player>
                <tr>
                    <td class="hide_on_small_screen">${player.id}</td>
                    <td>${player.league.club.name} &ndash; ${player.league.name} <#if player.currentDivision??>&ndash; ${player.currentDivision.name}</#if></td>
                    <td>${player.user.name}</td>
                    <td class="hide_on_small_screen">${player.status}</td>
                    <td class="button_column last"><a class="button" href="/player/delete/${player.id}">Delete</a><a class="button" href="/player/update/${player.id}">Modify</a></td>
                </tr>
            </#list>
            <tr class="create_row" id="create_player">
                <td class="last hide_on_small_screen"><#if (divisions?size > 0)>Division:<br/></#if>League:</td>
                <td class="last">
                    <#if (divisions?size > 0)>
                        <select id="currentDivision" name="currentDivision" title="${environment.getProperty("validation.player.currentDivision")}">
                            <option value="">${environment.getProperty("message.general.please_select")}</option>
                            <#list divisions as division>
                                <option value="${division.id}" <#if player.currentDivision?? && division.id == player.currentDivision.id>selected="selected"</#if>>${division.round.league.club.name} &ndash; ${division.round.league.name} &ndash; ${division.name}</option>
                            </#list>
                        </select>
                    </#if>
                    <select id="league" name="league" title="${environment.getProperty("validation.player.league")}">
                        <option value="">${environment.getProperty("message.general.please_select")}</option>
                        <#list leagues as league>
                            <option value="${league.id}" <#if player.league?? && league.id == player.league.id>selected="selected"</#if>>${league.club.name} &ndash; ${league.name}</option>
                        </#list>
                    </select>
                </td>
                <td class="last">
                    <#if (users?size > 0)>
                        <select id="user" name="user" required="required" title="${environment.getProperty("validation.player.user")}">
                            <option value="">${environment.getProperty("message.general.please_select")}</option>
                            <#list users as user>
                                <option value="${user.id}" <#if player.user?? && user.id == player.user.id>selected="selected"</#if>>${user.name}</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="hide_on_small_screen last">
                    <@spring.bind "playerStatuses" />
                    <@spring.formSingleSelectWithEmpty path="player.status" options=playerStatuses emptyValueMessage='${environment.getProperty("message.general.please_select")}' attributes='required="required" title="${environment.getProperty("validation.player.status")}"'/>
                </td>
                <td class="button_column last"><input type="submit" value="save"></td>
            </tr>
        </tbody>
    </table>
</form>

<p class="num_of_players">Number of Players: ${players?size}</p>

<h2 id="matches" class="table_title">Matches</h2>

<form method="post" action="/match/save">
    <@errors.print_binding_errors "match"/>
    <table class="action_table">
        <tbody class="strip_rows">
            <tr>
                <th class="hide_on_small_screen">Id</th>
                <th><label for="round">Round</label></th>
                <th><label for="playerOne">Player One</label></th>
                <th><label for="playerTwo">Player Two</label></th>
                <th>Score</th>
                <th class="hide_on_medium_screen">Score Entered</th>
                <th class="button_column last"></th>
            </tr>
            <#list matches as match>
                <tr>
                    <td class="hide_on_small_screen">${match.id}</td>
                    <td>${match.division.round.league.club.name} &ndash; ${match.division.round.league.name} &ndash; ${match.division.name} &ndash; (${match.division.round.startDate.toDate()?string("dd MMM yyyy")} &ndash; ${match.division.round.endDate.toDate()?string("dd MMM yyyy")})</td>
                    <td>${match.playerOne.user.name}</td>
                    <td>${match.playerTwo.user.name}</td>
                    <td>${match.score!""}</td>
                    <td class="hide_on_medium_screen"><#if match.scoreEntered??>${match.scoreEntered.toDate()?string("dd MMM yyyy")}</#if></td>
                    <td class="button_column last"><a class="button" href="/match/delete/${match.id}">Delete</a><a class="button" href="/match/update/${match.id}">Modify</a></td>
                </tr>
            </#list>
            <tr class="create_row" id="create_match">
                <td class="last hide_on_small_screen"></td>
                <td class="last">
                    <#if (divisions?size > 0)>
                        <select id="division" name="division" required="required" title="${environment.getProperty("validation.match.round")}">
                            <option value="">${environment.getProperty("message.general.please_select")}</option>
                            <#list divisions as division>
                                <option value="${division.id}" <#if match.division?? && division.id == match.division.id>selected="selected"</#if>>${division.round.league.club.name} &ndash; ${division.round.league.name} &ndash; (${division.round.startDate.toDate()?string("dd MMM yyyy")} &ndash; ${division.round.endDate.toDate()?string("dd MMM yyyy")})</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="last">
                    <#if (players?size > 0)>
                        <select id="playerOne" name="playerOne" required="required" title="${environment.getProperty("validation.match.playerOne")}">
                            <option value="">${environment.getProperty("message.general.please_select")}</option>
                            <#list players as player>
                                <option value="${player.id}" <#if match.playerOne?? && player.id == match.playerOne.id>selected="selected"</#if>>${player.user.name}</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="last">
                    <#if (players?size > 0)>
                        <select id="playerTwo" name="playerTwo" required="required" title="${environment.getProperty("validation.match.playerTwo")}">
                            <option value="">${environment.getProperty("message.general.please_select")}</option>
                            <#list players as player>
                                <option value="${player.id}" <#if match.playerTwo?? && player.id == match.playerTwo.id>selected="selected"</#if>>${player.user.name}</option>
                            </#list>
                        </select>
                    </#if>
                </td>
                <td class="last"><@spring.formInput  path="match.score" attributes='pattern="${scorePattern}" title="${environment.getProperty("validation.match.score")}" class="show_validation"'/></td>
                <td class="last"></td>
                <td class="button_column last"><input type="submit" value="save"></td>
            </tr>
        </tbody>
    </table>
</form>
</#macro>

<@page_html/>