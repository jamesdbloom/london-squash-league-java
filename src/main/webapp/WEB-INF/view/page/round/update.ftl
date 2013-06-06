<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Update Round</title>
</#macro>

<#macro content_header>
<div id="header">Update League Rounds</div>
</#macro>

<#macro content_section>
<style>
    #startDate:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.round.startDate")}";
    }

    #endDate:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.round.endDate")}";
    }
</style>
<form action="/leagueRounds/update" method="POST">

    <h2 class="table_subtitle" style="margin-left: 5em; margin-top: 3em;">${round.division.league.club.name} &ndash; ${round.division.league.name}</h2>

    <@errors.print_errors_list "round"/>
    <div class="standard_form">

        <input id="roundId" name="roundId" type="hidden" value="${round.id!""}">

        <p>
            <label for="startDate">Start Date:</label><input type="date" id="startDate" name="startDate" value="${startDate!""}" required="required" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"/> <span class="error_message"></span>
        </p>

        <p>
            <label for="endDate">End Date:</label><input type="date" id="endDate" name="endDate" value="${endDate!""}" required="required" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"/> <span class="error_message"></span>
        </p>

        <p class="submit">
            <input class="submit primary" type="submit" name="update" value="Update">
        </p>
    </div>
</form>
<script>
    var errors = errors || {},
            validation = {
                filled: ['startDate', 'endDate'],
                onload: errors && errors.round
            };
</script>
</#macro>

<@page_html/>
