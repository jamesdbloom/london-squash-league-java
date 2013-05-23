<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Register</title>
</#macro>

<#macro content_header>
<div id="header">Register</div>
</#macro>

<#macro content_section>
<style>
    #name:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.user.name")}";
    }

    #email:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.user.email")}";
    }

    #mobile:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.user.mobile")}";
    }

    #mobilePrivacy:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.user.mobilePrivacy")}";
    }

    #passwordOne:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.user.password")}";
    }

    #passwordTwo.invalid + .error_message::after {
        content: "${environment.getProperty("validation.user.passwordNonMatching")}";
    }
</style>
<form action="/register" method="POST">

    <p class="message">Please enter your details and you will receive an e-mailed to validate your email address.</p>

    <@errors.print_errors "user"/>
    <div class="standard_form">
        <p>
            <label for="name">Name:</label> <@spring.formInput path="user.name" attributes='required="required" pattern=".{3,25}" maxlength="25" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"' />
            <span class="error_message"></span>
        </p>

        <p>
            <label for="email">Email:</label> <@spring.formInput path="user.email" fieldType="email" attributes='required="required" pattern="${emailPattern}" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"' />
            <span class="error_message"></span>
        </p>

        <p>
            <label for="mobile">Mobile:</label> <@spring.formInput path="user.mobile" attributes='required="required" pattern="( *\\d *){6,15}" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"'/>
            <span class="error_message"></span>
        </p>

        <@spring.bind "mobilePrivacyOptions" />
        <p class="select">
            <label for="mobilePrivacy">Mobile Privacy:</label><@spring.formSingleSelectWithEmpty path="user.mobilePrivacy" options=mobilePrivacyOptions emptyValueMessage='${environment.getProperty("message.general.please_select")}' attributes='required="required"' />
            <span class="error_message"></span>
        </p>

        <div style="width:100%; height: 1.5em;"></div>

        <p>
            <label class="user_password" for="passwordOne">Password One:</label> <input type="password" id="passwordOne" name="passwordOne" value="${passwordOne!""}" required="required" pattern="${passwordPattern}" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"/> <span class="error_message"></span>
        </p>

        <p>
            <label class="user_password" for="passwordTwo">Password Two:</label> <input type="password" id="passwordTwo" name="passwordTwo" value="${passwordTwo!""}" required="required" pattern="${passwordPattern}" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"/> <span class="error_message"></span>
        </p>

        <div style="width:100%; height: 1em;"></div>

        <p class="submit">
            <input class="submit primary" type="submit" formnovalidate name="register" value="Register">
        </p>
    </div>
</form>
<script>
    var errors = errors || {},
            validation = {
                filled: ['name', 'email', 'mobile', 'passwordOne'],
                changed: ['mobilePrivacy'],
                matches: [
                    {id: 'passwordTwo', matches: 'passwordOne'}
                ],
                onload: errors && errors.user
            };
</script>
</#macro>

<@page_html/>
