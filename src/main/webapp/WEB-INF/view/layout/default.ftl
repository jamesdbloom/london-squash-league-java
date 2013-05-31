<#--<#ftl strip_whitespace=true strict_syntax=true strip_text=true />-->
<#setting url_escaping_charset="UTF-8" />
<#import "/WEB-INF/view/macro/spring_extension.ftl" as spring />
<#import "/WEB-INF/view/furniture/errors.ftl" as errors />
<#import "/WEB-INF/view/furniture/navigation.ftl" as navigation />

<#macro page_html>
    <#--<@compress single_line=true>-->
        <#escape x as x?html>
            <#if isAjax?? && isAjax>
                <@page_body/>
            <#else>
            <!DOCTYPE html>
            <html lang="en_GB">
                <head>
                    <@page_head/>
                </head>
                <#flush>
                <body onunload="">
                    <@page_body/>
                    <@page_js/>
                </body>
            </html>
            </#if>
        </#escape>
    <#--</@compress>-->
</#macro>

<#macro page_head>
    <@page_meta/>
    <@page_title/>
    <@page_css/>
<link rel="shortcut icon" href="/resources/icon/favicon.ico"/>
<link rel="apple-touch-icon" href="/resources/icon/apple-touch-icon.png"/>
</#macro>

<#macro page_meta>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=yes"/>
<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/>
<meta name="format-detection" content="telephone=no"/>
<meta name="description" content="some silly description to have on this page"/>
<meta name="apple-mobile-web-app-status-bar-style" content="black"/>
</#macro>

<#macro page_css>
    <#list cssResources["all"] as cssFile>
    <link rel="stylesheet" type="text/css" href="${cssFile}">
    </#list>
</#macro>

<#macro page_body>
<div id="container">
    <@content_header/>
    <@navigation.navigation/>
    <div id="main_content">
        <div class="section">
            <@content_section/>
        </div>
    </div>
    <div id="footer"><p><a href="http://blog.jamesdbloom.com/">&copy; 2012 James D Bloom</a></p></div>
</div>
</#macro>

<#macro page_title>
<title>London Squash League<!-- page title appended in here --></title>
</#macro>

<#macro content_header>
<div id="header"><!-- page header in here --></div>
</#macro>

<#macro content_section>
<!-- page content in here -->
</#macro>

<#macro page_js>
<script type="text/javascript">
    window.onload = function () {
        setTimeout(function () {
            <#list jsResources["all"] as jsFile>
                <#local node = "node_${jsFile_index}">
                var ${node} =
                document.createElement('script');
            ${node}.
                setAttribute('type', 'text/javascript');
            ${node}.
                setAttribute('src', '${jsFile}');
                document.body.appendChild(${node});
            </#list>
        }, 50);
    };
</script>
    <@page_js_inline/>
<!-- google analytics start -->
<!-- <script type="text/javascript">
    var _gaq = _gaq || [];
    _gaq.push(['_setAccount', 'UA-32687194-3']);
    _gaq.push(['_trackPageview']);
    (function () {
        var ga = document.createElement('script');
        ga.type = 'text/javascript';
        ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(ga, s);
    })();
</script>-->
<!-- google analytics end -->
</#macro>

<#macro page_js_inline>

</#macro>
