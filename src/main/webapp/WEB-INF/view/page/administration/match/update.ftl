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

    #round:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.match.round")}";
    }
</style>
<form action="/match/update" method="POST">

    <@errors.print_errors "match"/>
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
            <label class="round" for="round">Round:</label>
            <#if (rounds?size > 0)>
                <select id="round" name="round" required="required" title="${environment.getProperty("validation.match.round")}">
                    <option value="">${environment.getProperty("message.general.please_select")}</option>
                    <#list rounds as round>
                        <option value="${round.id}" <#if (match.round?? && match.round.id == round.id)>selected="selected"</#if>>${round.division.league.club.name} &ndash; ${round.division.league.name} &ndash; ${round.division.name} &ndash; (${round.startDate.toDate()?string("dd MMM yyyy")} &ndash; ${round.endDate.toDate()?string("dd MMM yyyy")})</option>
                    </#list>
                </select> <span class="error_message"></span>
            </#if>
        </p>

        <p class="submit">
            <input class="submit primary" type="submit" name="update" value="Update">
        </p>
    </div>
</form>
<script>
    var errors = errors || {},
            validation = {
                changed: ['playerOne', 'playerTwo', 'round'],
                onload: errors && errors.match
            };
</script>
</#macro>

<@page_html/>
