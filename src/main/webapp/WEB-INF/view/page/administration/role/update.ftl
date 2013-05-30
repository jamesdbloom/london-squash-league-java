<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Update Role</title>
</#macro>

<#macro content_header>
<div id="header">Update Role</div>
</#macro>

<#macro content_section>
<style>
    #name:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.role.name")}";
    }

    #description:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.role.description")}";
    }
</style>
<form action="/role/update" method="POST">

    <@errors.print_binding_errors "role"/>
    <div class="standard_form">

        <p>
            <label class="id" for="id">Id:</label><input id="id" name="id" type="text" value="${role.id!""}" readonly="readonly">
        </p>
        <input id="version" name="version" type="hidden" value="${role.version!""}">

        <p>
            <label for="name">Name:</label><@spring.formInput path="role.name" attributes='required="required" pattern=".{5,25}" maxlength="25" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"' />
            <span class="error_message"></span>
        </p>

        <p>
            <label for="description">Description:</label><@spring.formInput path="role.description" attributes='required="required" pattern=".{5,50}" maxlength="50" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"' />
            <span class="error_message"></span>
        </p>

        <p class="submit">
            <input class="submit primary" type="submit" name="update" value="Update">
        </p>
    </div>
</form>
<script>
    var errors = errors || {},
            validation = {
                filled: ['name', 'description'],
                onload: errors && errors.role
            };
</script>
</#macro>

<@page_html/>
