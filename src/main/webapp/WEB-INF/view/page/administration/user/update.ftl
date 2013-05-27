<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Update User</title>
</#macro>

<#macro content_header>
<div id="header">Update User</div>
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

    #roles:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.user.roles")}";
    }
</style>
<form action="/user/update" method="POST">

    <@errors.print_errors "user"/>
    <div class="standard_form">

        <p>
            <label class="id" for="id">Id:</label><input id="id" name="id" type="text" value="${user.id!""}" readonly="readonly">
        </p>
        <input id="version" name="version" type="hidden" value="${user.version!""}">

        <p>
            <label for="name">Name:</label> <@spring.formInput path="user.name" attributes='required="required" pattern=".{3,50}" maxlength="50" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"' />
            <span class="error_message"></span>
        </p>

        <p>
            <label for="email">Email:</label> <@spring.formInput path="user.email" fieldType="email" attributes='required="required" pattern="${emailPattern}" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"' />
            <span class="error_message"></span>
        </p>

        <p>
            <label for="mobile">Mobile:</label> <@spring.formInput path="user.mobile" attributes='required="required" pattern="( *\\d *){6,15}" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"' />
            <span class="error_message"></span>
        </p>

        <@spring.bind "mobilePrivacyOptions" />
        <p class="select">
            <label for="mobilePrivacy">Mobile Privacy:</label><@spring.formSingleSelectWithEmpty path="user.mobilePrivacy" options=mobilePrivacyOptions emptyValueMessage='${environment.getProperty("message.general.please_select")}' attributes='required="required"' />
            <span class="error_message"></span>
        </p>

        <p class="select">
            <label class="roles" for="roles">Roles:</label>
            <@security.authorize access='hasRole("ROLE_ADMIN")'>
                <#if (roles?size > 0)>
                    <select id="roles" name="roles" <#if (roles?size > 1)>multiple="multiple"</#if> required="required" title="${environment.getProperty("validation.user.roles")}">
                        <#if (roles?size <= 1)>
                            <option value="">${environment.getProperty("message.general.please_select")}</option>
                        </#if>
                        <#list roles as role>
                            <option value="${role.name}" <#if user.hasRole(role) >selected="selected"</#if>>${role.description}</option>
                        </#list>
                    </select> <span class="error_message"></span>
                </#if>
            </@security.authorize>
            <@security.authorize access='!hasRole("ROLE_ADMIN")'>
                <input id="roles" name="roles" type="text" value="<#list user.roles as role>${role.name}<#if role_has_next>, </#if></#list>" readonly="readonly">
            </@security.authorize>
        </p>

        <p class="submit">
            <input class="submit primary" type="submit" formnovalidate name="save" value="Save">
        </p>
    </div>
</form>
<script>
    var errors = errors || {},
            validation = {
                filled: ['name', 'email', 'mobile', 'password'],
                changed: ['mobilePrivacy', 'roles'],
                onload: errors && errors.user
            };
</script>
</#macro>

<@page_html/>
