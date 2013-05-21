<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />

<#macro navigation>
<ul class="tabs">
    <li><a href="/" title="Home">Home</a></li>
    <@security.authorize access='isAnonymous()'>
        <li><a href="/login" title="Login">Login</a></li>
        <li><a href="/register" title="Register">Register</a></li>
        <li><a href="/retrieve_password" title="Lost Password?">Lost Password?</a></li>
    </@security.authorize>

    <@security.authorize access='isAuthenticated()'>
        <@security.authorize access='hasAnyRole("ROLE_ADMIN")'>
            <li class="hide_on_small_screen "><a href="/print" title="Print League">Print League</a></li>
        </@security.authorize>
        <li><a href="/league" title="View League">View League</a></li>
        <li class="hide_on_small_screen "><a href="/ranking" title="Player Ranking">Player Ranking</a></li>
        <li><a href="/account" title="Account">Account</a></li>
        <@security.authorize access='hasAnyRole("ROLE_ADMIN")'>
            <li class="hide_on_medium_screen "><a href="/administration" title="Administration">Administration</a></li>
        </@security.authorize>
        <li class="hide_on_small_screen "><a href="/contact_us" title="Contact Us">Contact Us</a></li>
        <li><a href="/logout" title="Logout">Logout</a></li>
    </@security.authorize>
</ul>
</#macro>