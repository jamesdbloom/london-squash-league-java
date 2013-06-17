<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Update Match</title>
</#macro>

<#macro content_header>
<div id="header">Update Match</div>
</#macro>

<#macro content_section>
<style>
    #playerOne:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.match.playerOne")}";
    }

    #playerTwo:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.match.playerTwo")}";
    }

    #division:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.match.round")}";
    }

    #score:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.match.score")}";
    }
</style>
<form action="/match/update" method="POST">

    <@errors.print_binding_errors "match"/>
    <div class="standard_form">

        <p>
            <label class="id" for="id">Id:</label><input id="id" name="id" type="text" value="${match.id!""}" readonly="readonly">
        </p>
        <input id="version" name="version" type="hidden" value="${match.version!""}">

        <p class="select">
            <label for="playerOne">Player One:</label>
            <#if (players?size > 0)>
                <select id="playerOne" name="playerOne" required="required" title="${environment.getProperty("validation.match.playerOne")}">
                    <option value="">${environment.getProperty("message.general.please_select")}</option>
                    <#list players as player>
                        <option value="${player.id}" <#if (match.playerOne?? && match.playerOne.id == player.id)>selected="selected"</#if>>${player.user.name}</option>
                    </#list>
                </select> <span class="error_message"></span>
            </#if>
        </p>

        <p class="select">
            <label for="playerTwo">Player Two:</label>
            <#if (players?size > 0)>
                <select id="playerTwo" name="playerTwo" required="required" title="${environment.getProperty("validation.match.playerTwo")}">
                    <option value="">${environment.getProperty("message.general.please_select")}</option>
                    <#list players as player>
                        <option value="${player.id}" <#if (match.playerTwo?? && match.playerTwo.id == player.id)>selected="selected"</#if>>${player.user.name}</option>
                    </#list>
                </select> <span class="error_message"></span>
            </#if>
        </p>

        <p class="select">
            <label class="division" for="division">Round:</label>
            <#if (divisions?size > 0)>
                <select id="division" name="division" required="required" title="${environment.getProperty("validation.match.round")}">
                    <option value="">${environment.getProperty("message.general.please_select")}</option>
                    <#list divisions as division>
                        <option value="${division.id}" <#if (match.division?? && match.division.id == division.id)>selected="selected"</#if>>${division.round.league.club.name} &ndash; ${division.round.league.name} &ndash; ${division.name} &ndash; (${division.round.startDate.toDate()?string("dd MMM yyyy")} &ndash; ${division.round.endDate.toDate()?string("dd MMM yyyy")})</option>
                    </#list>
                </select> <span class="error_message"></span>
            </#if>
        </p>


        <p>
            <label for="score">Score:</label> <@spring.formInput  path="match.score" attributes='pattern="[0-9]{1,2}-[0-9]{1,2}" maxlength="5" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"'/>
            <span class="error_message"></span>
        </p>

        <p>
            <label for="scoreEntered">Score Entered:</label><input id="scoreEntered" type="text" value="<#if match.scoreEntered??>${match.scoreEntered.toDate()?string("dd MMM yyyy")}</#if>" readonly="readonly">
        </p>

        <p class="submit">
            <input class="submit primary" type="submit" name="update" value="Update">
        </p>
    </div>
</form>
<script>
    var errors = errors || {},
            validation = {
                filled: ['score'],
                changed: ['playerOne', 'playerTwo', 'division'],
                onload: errors && errors.match
            };
</script>
</#macro>

<@page_html/>
