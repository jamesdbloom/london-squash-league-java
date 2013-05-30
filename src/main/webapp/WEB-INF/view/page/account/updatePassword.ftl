<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Update Password</title>
</#macro>

<#macro content_header>
<div id="header">Update Password</div>
</#macro>

<#macro content_section>
<style>
    #newPassword:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.user.password")}";
    }

    #passwordConfirm.invalid + .error_message::after {
        content: "${environment.getProperty("validation.user.passwordNonMatching")}";
    }
</style>
<form action="/account/updatePassword" method="POST">

    <p class="message">${environment.getProperty("validation.user.password")}</p>

    <@errors.print_errors_list "password"/>
    <div class="standard_form">
        <p>
            <label for="existingPassword">Existing Password:</label> <input type="password" id="existingPassword" name="existingPassword" value="" required="required" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"/> <span class="error_message"></span>
        </p>

        <p>
            <label for="newPassword">New Password:</label> <input type="password" id="newPassword" name="newPassword" value="" required="required" pattern="${passwordPattern}" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"/> <span class="error_message"></span>
        </p>

        <p>
            <label for="passwordConfirm">New Password Confirm:</label> <input type="password" id="passwordConfirm" name="passwordConfirm" value="" required="required" pattern="${passwordPattern}" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"/> <span class="error_message"></span>
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
                filled: ['existingPassword', 'newPassword'],
                matches: [
                    {id: 'passwordConfirm', matches: 'password'}
                ],
                onload: errors && errors.password
            };
</script>
</#macro>

<@page_html/>
