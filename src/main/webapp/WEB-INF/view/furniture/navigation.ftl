<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />

<#macro navigation>
<ul class="tabs">
    <li <#if page == "">class="active"</#if>><a href="/">Home</a></li>
    <@security.authorize access='isAnonymous()'>
        <li <#if page == "login">class="active"</#if>><a href="/login">Login</a></li>
        <li <#if page == "register">class="active"</#if>><a href="/register">Register</a></li>
        <li <#if page == "retrievePassword">class="active"</#if>><a href="/retrievePassword">Lost Password?</a></li>
    </@security.authorize>

    <@security.authorize access='isAuthenticated()'>
        <@security.authorize access='hasAnyRole("ROLE_ADMIN")'>
            <li class="hide_on_small_screen <#if page == "print">active</#if>"><a href="/print">Print League</a></li>
            <li class="hide_on_small_screen <#if page == "leagueRounds">active</#if>"><a href="/leagueRounds">Create Round</a></li>
            <li class="hide_on_medium_screen <#if page == "administration">active</#if>"><a href="/administration">Administration</a></li>
        </@security.authorize>
        <li <#if page == "leagueTable">class="active"</#if>><a href="/leagueTable">View League</a></li>
        <li <#if page == "ranking">class="active"</#if>><a href="/ranking">Player Ranking</a></li>
        <li <#if page == "account">class="active"</#if>><a href="/account">Account</a></li>
        <li class="hide_on_small_screen <#if page == "contact_us">active</#if>"><a href="/contact_us">Contact Us</a></li>
        <li class="hide_on_small_screen <#if page == "logout">active</#if>"><a href="/logout">Logout</a></li>
    </@security.authorize>
</ul>
</#macro>