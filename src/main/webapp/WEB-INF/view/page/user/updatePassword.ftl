<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Update Password</title>
</#macro>

<#macro content_header>
<div id="header">Update Password</div>
</#macro>

<#macro content_section>
<style>
    #password:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.user.password")}";
    }

    #passwordConfirm.invalid + .error_message::after {
        content: "${environment.getProperty("validation.user.passwordNonMatching")}";
    }
</style>
<form action="/updatePassword" method="POST">

    <p class="message">${environment.getProperty("validation.user.password")}</p>

    <@errors.print_errors_list "password"/>
    <div class="standard_form">
        <input id="email" name="email" type="hidden" value="${email!""}">
        <input id="oneTimeToken" name="oneTimeToken" type="hidden" value="${oneTimeToken!""}">

        <p>
            <label for="password">Password:</label> <input type="password" id="password" name="password" value="" required="required" pattern="${passwordPattern}" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"/> <span class="error_message"></span>
        </p>

        <p>
            <label for="passwordConfirm">Password Confirm:</label> <input type="password" id="passwordConfirm" name="passwordConfirm" value="" required="required" pattern="${passwordPattern}" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"/> <span class="error_message"></span>
        </p>

        <div style="width:100%; height: 1em;"></div>

        <p class="submit">
            <input class="submit primary" type="submit" formnovalidate name="submit" value="Update Password">
        </p>
    </div>
</form>
<script>
    var errors = errors || {},
            validation = {
                filled: ['password'],
                matches: [
                    {id: 'passwordConfirm', matches: 'password'}
                ],
                onload: errors && errors.password
            };
</script>
</#macro>

<@page_html/>
