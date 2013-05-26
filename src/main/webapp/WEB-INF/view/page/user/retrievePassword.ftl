<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Update Password</title>
</#macro>

<#macro content_header>
<div id="header">Update Password</div>
</#macro>

<#macro content_section>
<style>
    #email:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.user.email")}";
    }
</style>
<form action="/sendUpdatePasswordEmail" method="POST">

    <p class="message">Please enter your email address and you will be emailed a link to update your password.</p>

    <div class="standard_form">
        <p>
            <label for="email">Email:</label> <input type="text" id="email" name="email" value="" required="required" pattern="${emailPattern}" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"/>
            <span class="error_message"></span>
        </p>

        <div style="width:100%; height: 1em;"></div>

        <p class="submit">
            <input class="submit primary" type="submit" formnovalidate name="submit" value="Retrieve Password">
        </p>
    </div>
</form>
<script>
    var errors = errors || {},
            validation = {
                filled: ['email']
            };
</script>
</#macro>

<@page_html/>
