<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />

<#macro navigation>
<ul class="tabs">
    <li><a href="/">Home</a></li>
    <@security.authorize access='isAnonymous()'>
        <li><a href="/login">Login</a></li>
        <li><a href="/register">Register</a></li>
        <li><a href="/retrievePassword">Lost Password?</a></li>
    </@security.authorize>

    <@security.authorize access='isAuthenticated()'>
        <@security.authorize access='hasAnyRole("ROLE_ADMIN")'>
            <li class="hide_on_small_screen"><a href="/print">Print League</a></li>
            <li class="hide_on_small_screen"><a href="/leagueRounds">Create Round</a></li>
            <li class="hide_on_medium_screen"><a href="/administration">Administration</a></li>
        </@security.authorize>
        <li><a href="/leagueTable">View League</a></li>
        <li><a href="/ranking">Player Ranking</a></li>
        <li><a href="/account">Account</a></li>
        <li class="hide_on_small_screen"><a href="/contact_us">Contact Us</a></li>
        <li class="hide_on_small_screen"><a href="/logout">Logout</a></li>
    </@security.authorize>
</ul>
</#macro>