<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Enter Score</title>
</#macro>

<#macro content_header>
<div id="header">Enter Score</div>
</#macro>

<#macro content_section>
<form action="/score" method="POST">

    <p style="margin: 3%">Please enter the score for your match <strong>${match.playerOne.user.name}</strong> vs <strong>${match.playerOne.user.name}</strong><br/><br/>Note: please use format X-Y where X is the number of games won by <strong>${match.playerOne.user.name}</strong> and Y is the number of games won by <strong>${match.playerOne.user.name}</strong>.</p>

    <@errors.print_errors_list "score"/>
    <div class="standard_form">
        <input id="referer" name="referer" type="hidden" value="${referer!""}">
        <input id="id" name="id" type="hidden" value="${match.id}">

        <p>
            <label for="score">Score:</label> <input type="score" id="score" name="score" value="${match.score!""}" required="required" pattern="${scorePattern}" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"/> <span class="error_message"></span>
        </p>

        <div style="width:100%; height: 1em;"></div>

        <p class="submit">
            <input class="submit primary" type="submit" formnovalidate name="submit" value="Submit Score">
        </p>
    </div>
</form>
<script>
    var errors = errors || {},
            validation = {
                filled: ['score'],
                onload: errors && errors.score
            };
</script>
</#macro>

<@page_html/>