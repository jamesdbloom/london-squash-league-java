<#include "/WEB-INF/view/layout/default.ftl" />

<#macro page_title>
<title>London Squash League - Contact Us</title>
</#macro>

<#macro content_header>
<div id="header">Contact Us</div>
</#macro>

<#macro content_section>
<form action="/contact_us" method="post">

        <p class="message">To contact us about anything else please enter a message below and we will get back to you as soon as we can.</p>

        <div class="standard_form">
            <p>
                <label class="email" for="email">Your Email:</label>
                <input id="email" type="email" name="email" readonly="readonly" value="${user.email}">

            </p>

            <p>
                <label class="contact_message" for="message">Message:</label>
                <textarea id="message" name="message" cols="58" rows="15" maxlength="2048" required="required"></textarea>
            </p>

            <p class="submit">
                <input class="submit primary" type="submit" name="submit" value="Send Message">
            </p>
        </div>
    </form>
</#macro>

<@page_html/>