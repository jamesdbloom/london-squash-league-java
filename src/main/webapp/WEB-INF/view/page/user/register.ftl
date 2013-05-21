<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Register</title>
</#macro>

<#macro content_header>
<div id="header">Register</div>
</#macro>

<#macro content_section>
<style>
    .error_message::after {
        float: right;
        width: 60%;
        margin: 1em;
        text-align: center;
        border-color: rgba(255, 97, 97, 0.51);
        border-style: dashed;
        border-width: 1px;
        padding: 0.5em;
    }
</style>
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
<form action="/register" method="POST" novalidate="novalidate">

    <p class="message">Please enter your details and you will receive an e-mailed to validate your email address.</p>

    <#assign filledFunction = "if(this.value.length >= 1){ this.setAttribute('class', 'filled'); } else { this.setAttribute('class', 'empty'); }" />

    <@errors.print_errors "user"/>
    <div class="register_form">
        <p>
            <label class="user_name" for="name">Name:</label> <@spring.formInput path="user.name" attributes='required="required" pattern=".{3,25}" title="${environment.getProperty("validation.user.name")}" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off" onblur="${filledFunction}"' />
            <span class="error_message"></span>
        </p>

        <p>
            <label class="user_email" for="email">Email:</label> <@spring.formInput path="user.email" attributes='required="required" pattern="${emailPattern}" title="${environment.getProperty("validation.user.email")}" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off" onblur="${filledFunction}"' />
            <span class="error_message"></span>
        </p>

        <p>
            <label class="user_mobile" for="mobile">Mobile:</label> <@spring.formInput path="user.mobile" attributes='required="required" pattern="[\\d\\s]{6,15}" title="${environment.getProperty("validation.user.mobile")}" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off" onblur="${filledFunction}"'/>
            <span class="error_message"></span>
        </p>

        <@spring.bind "mobilePrivacyOptions" />
        <p class="select">
            <label class="user_mobile_private" for="mobilePrivacy">Mobile Privacy:</label>
            <@spring.formSingleSelectWithEmpty path="user.mobilePrivacy" options=mobilePrivacyOptions emptyValueMessage="Please select" attributes='required="required" title="${environment.getProperty("validation.user.mobilePrivacy")}" onblur="this.setAttribute(\'class\', \'filled\');"' />
        </p>

        <div style="width:100%; height: 1.5em;"></div>

        <p>
            <label class="user_password" for="passwordOne">Password One:</label> <input type="password" id="passwordOne" name="passwordOne" value="${passwordOne!""}" required='required' pattern='${passwordPattern}' title="${environment.getProperty("validation.user.password")}" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off" onblur="${filledFunction}"> <span class="error_message"></span>
        </p>

        <p>
            <label class="user_password" for="passwordTwo">Password Two:</label> <input type="password" id="passwordTwo" name="passwordTwo" value="${passwordTwo!""}" required='required' pattern='${passwordPattern}' title="${environment.getProperty("validation.user.passwordNonMatching")}" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off" onblur="if(this.value.length == 0 || this.value == document.getElementById('passwordOne').value) { this.setAttribute('class', 'valid'); } else { this.setAttribute('class', 'invalid'); }">
            <span class="error_message"></span>
        </p>

        <div style="width:100%; height: 1em;"></div>

        <p class="submit">
            <input class="submit primary" type="submit" name="save" value="Save">
        </p>
    </div>
</form>
</#macro>

<@page_html/>
