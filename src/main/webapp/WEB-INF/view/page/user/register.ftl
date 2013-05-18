<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Register</title>
</#macro>

<#macro content_header>
<div id="header">Register</div>
</#macro>

<#macro content_section>
<form action="/register" method="POST">

    <p class="message">Please enter your details and a temporary password will be e-mailed to you.</p>

    <@errors.print_errors "user"/>
    <div class="register_form">
        <p>
            <label class="user_name" for="name">Name:</label> <@spring.formInput path="user.name" attributes="tabindex='1' required='required' pattern='.{3,25}' class='show_validation' autocorrect='off' autocapitalize='off' autocomplete='off'" />
        </p>

        <p>
            <label class="user_email" for="email">Email:</label> <@spring.formInput path="user.email" attributes="tabindex='2' required='required' class='show_validation' autocorrect='off' autocapitalize='off' autocomplete='off'" />
        </p>

        <p>
            <label class="user_mobile" for="mobile">Mobile:</label> <@spring.formInput path="user.mobile" attributes="tabindex='3' required='required' pattern='\\d{5,25}' class='show_validation' autocorrect='off' autocapitalize='off' autocomplete='off'" />
        </p>

        <@spring.bind "mobilePrivacyOptions" />
        <p class="select">
            <label class="user_mobile_private" for="mobilePrivate">Mobile Privacy:</label>
            <@spring.formSingleSelectWithEmpty path="user.mobilePrivate" options=mobilePrivacyOptions emptyValueMessage="Please select" attributes="tabindex='4' required='required'" />
        </p>

        <p class="submit">
            <input class="submit primary" type="submit" name="save" value="Save" tabindex="5">
        </p>
    </div>
</form>
</#macro>

<@page_html/>
