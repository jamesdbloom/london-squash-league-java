<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Contact Us</title>
</#macro>

<#macro content_header>
<div id="header">Contact Us</div>
</#macro>

<#macro content_section>
<form action="/contact_us" method="post">

        <p class="message">To report an issue, defect or bug on the site please use the <a href="/report_issue" title="Report Issue">Report Issue</a> page, to contact us about anything else please enter a message below and we will get back to you as soon as we can</p>

        <div class="send_message_form">

            <p>
                <label class="email" for="email">Your Email:</label>
                <input id="email" type="email" name="email" readonly="readonly" value="${user.email}">

            </p>

            <p>
                <label class="contact_message" for="message">Message:</label>
                <textarea id="message" name="message" class="show_validation" cols="58" rows="15" required="required" pattern="^[a-zA-Z0-9\\s._-]{1,500}$" autocorrect="”off”" autocapitalize="”off”" autocomplete="”off”" required="required" tabindex="10"></textarea>
            </p>

            <p class="submit">
                <input class="submit primary" type="submit" name="submit" value="Send Message" tabindex="100">
            </p>
        </div>
    </form>
</#macro>

<@page_html/>