<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Update User</title>
</#macro>

<#macro content_header>
<div id="header">Update User</div>
</#macro>

<#macro content_section>
<form action="/user/update" method="POST">
    <@errors.print_errors "user"/>

    <div class="standard_form">

        <p>
            <label class="id" for="id">Id:</label><input id="id" name="id" type="text" value="${user.id!""}" readonly="readonly">
        </p>
        <input id="version" name="version" type="hidden" value="${user.version!""}">

        <p>
            <label class="name" for="name">Name:</label> <@spring.formInput path="user.name" attributes='required="required" pattern=".{3,25}" maxlength="25" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"' />
        </p>

        <p>
            <label class="email" for="email">Email:</label> <@spring.formInput path="user.email" fieldType="email" attributes='required="required" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"' />
        </p>

        <p>
            <label class="mobile" for="mobile">Mobile:</label> <@spring.formInput path="user.mobile" attributes='required="required" pattern="[\\d\\s]{6,15}" maxlength="15" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"' />
        </p>

        <@spring.bind "mobilePrivacyOptions" />
        <p class="select">
            <label class="user_mobile_private" for="mobilePrivacy">Mobile Privacy:</label>
            <@spring.formSingleSelectWithEmpty path="user.mobilePrivacy" options=mobilePrivacyOptions emptyValueMessage='${environment.getProperty("message.general.please_select")}' attributes='required="required"' />
        </p>

        <p class="select">
            <label class="roles" for="roles">Roles:</label>
            <@security.authorize access='hasRole("ROLE_ADMIN")'>
                <#if (roles?size > 0)>
                    <select id="roles" name="roles" <#if (roles?size > 1)>multiple="multiple"</#if> required="required">
                        <#if (roles?size <= 1)>
                            <option value="">${environment.getProperty("message.general.please_select")}</option>
                        </#if>
                        <#list roles as role>
                            <option value="${role.id}" <#if user.hasRole(role) >selected="selected"</#if>>${role.description}</option>
                        </#list>
                    </select>
                </#if>
            </@security.authorize>
            <@security.authorize access='!hasRole("ROLE_ADMIN")'>
                <input id="roles" name="roles" type="text" value="<#list user.roles as role>${role.name}<#if role_has_next>, </#if></#list>" readonly="readonly">
            </@security.authorize>
        </p>

        <p class="submit">
            <input class="submit primary" type="submit" name="save" value="Save">
        </p>
    </div>
</form>
</#macro>

<@page_html/>
