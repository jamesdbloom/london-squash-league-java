<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Login</title>
</#macro>

<#macro content_header>
<div id="header">Login</div>
</#macro>

<#macro content_section>
<ol class="link_list">
    <li><a href="/register" title="1. Register">1. Register</a></li>
    <li><a href="/account#divisions" title="2. Join A League">2. Join A League</a></li>
</ol>
<form name="spring_security_check_form" action="/j_spring_security_check" method="POST">

    <input type="hidden" name="redirect_to" value="/">

    <#if SPRING_SECURITY_LAST_EXCEPTION??>
      <p class="errors_warnings">Login Failed:&nbsp;&nbsp;&nbsp;${SPRING_SECURITY_LAST_EXCEPTION.message}</p>
    </#if>

    <div class="login_form">
        <p>
            <label class="email" for="email">E-mail:</label> <input id="email" type="email" name="email" value="" autocorrect="off" autocapitalize="off" autocomplete="off" required="required">
        </p>

        <p>
            <label class="password" for="password">Password:</label> <input id="password" type="password" name="password" value="" autocorrect="off" autocapitalize="off" autocomplete="off" required="required">
        </p>

        <#-- TODO can I easily implement a remember cookie for username? -->
        <#--<p>-->
            <#--<label class="remember_me" for="remember_me">Remember:</label> <input id="remember_me" name="remember_me" type="checkbox" value="forever">-->
        <#--</p>-->

        <p class="submit">
            <input class="submit primary" type="submit" name="submit" value="Login">
        </p>
    </div>
</form>
</#macro>

<@page_html/>