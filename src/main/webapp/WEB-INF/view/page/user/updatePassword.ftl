<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Update Password</title>
</#macro>

<#macro content_header>
<div id="header">Update Password</div>
</#macro>

<#macro content_section>
<style>
    #passwordOne:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.user.password")}";
    }

    #passwordTwo.invalid + .error_message::after {
        content: "${environment.getProperty("validation.user.passwordNonMatching")}";
    }
</style>
<form action="/updatePassword" method="POST">
    <#if validationErrors?? && (validationErrors?size > 0)>
        <div id="validation_error_password" class="errors_warnings">
            <p>There were problems with the data you entered:</p>
            <#list validationErrors as error>
                <p class="validation_error" style="margin-left: 2em;">&ndash; ${error}</p>
            </#list>
        </div>
        <script>
            var errors = errors || {};
            errors.password = true;
        </script>
    </#if>
    <div class="standard_form">
        <input id="email" name="email" type="hidden" value="${email!""}">
        <input id="oneTimeToken" name="oneTimeToken" type="hidden" value="${oneTimeToken!""}">

        <p>
            <label class="user_password" for="passwordOne">Password One:</label> <input type="password" id="passwordOne" name="passwordOne" value="${passwordOne!""}" required="required" pattern="${passwordPattern}" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"/> <span class="error_message"></span>
        </p>

        <p>
            <label class="user_password" for="passwordTwo">Password Two:</label> <input type="password" id="passwordTwo" name="passwordTwo" value="${passwordTwo!""}" required="required" pattern="${passwordPattern}" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"/> <span class="error_message"></span>
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
                filled: ['passwordOne'],
                matches: [
                    {id: 'passwordTwo', matches: 'passwordOne'}
                ],
                onload: errors && errors.password
            };
</script>
</#macro>

<@page_html/>
