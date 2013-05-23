<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Update Club</title>
</#macro>

<#macro content_header>
<div id="header">Update Club</div>
</#macro>

<#macro content_section>
<style>
    #name:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.club.name")}";
    }

    #address:invalid.filled + .error_message::after {
        content: "${environment.getProperty("validation.club.address")}";
    }
</style>
<form action="/club/update" method="POST">

    <@errors.print_errors "club"/>
    <div class="standard_form">

        <p>
            <label class="id" for="id">Id:</label><input id="id" name="id" type="text" value="${club.id!""}" readonly="readonly">
        </p>
        <input id="version" name="version" type="hidden" value="${club.version!""}">

        <p>
            <label for="name">Name:</label><@spring.formInput path="club.name" attributes='required="required" pattern=".{5,25}" maxlength="25" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"' />
            <span class="error_message"></span>
        </p>

        <p>
            <label for="address">Address:</label><@spring.formInput path="club.address" attributes='required="required" pattern=".{5,50}" maxlength="50" class="show_validation" autocorrect="off" autocapitalize="off" autocomplete="off"' />
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
                filled: ['name', 'address'],
                onload: errors && errors.club
            };
</script>
</#macro>

<@page_html/>
