<#include "/spring.ftl" />

<#--
 * message
 *
 * Macro to translate a message code into a message,
 * where [[code]] is used if it does not exist,
 * where {{code}} is used if it does exist and is blank
  -->
<#function getMessage code>
    <#local message = springMacroRequestContext.getMessage(code, "[["+code+"]]")>
    <#if message == "" >
        <#return "{{"+code+"}}" />
    <#else>
        <#return message />
    </#if>
</#function>

<#macro message code>
    <#local message = getMessage(code)>
    ${message}
</#macro>

<#--
 * message with arguments
 *
 * Macro to translate a message code into a message,
 * where [[code]] is used if it does not exist,
 * where {{code}} is used if it does exist and is blank
  -->
<#function getMessageWithArgs code args>
    <#local message = springMacroRequestContext.getMessage(code, "[["+code+"]]", args)>
    <#if message == "" >
        <#return "{{"+code+"}}" />
    <#else>
        <#return message />
    </#if>
</#function>

<#macro messageWithArgs code args>
    <#local message = getMessageWithArgs(code, args)>
    ${message}
</#macro>

<#--
 * formSingleSelectWithEmpty
 *
 * Extends spring formSingleSelect with ability to have default empty value
 *
 * @param path the name of the field to bind to
 * @param options a map (value=label) of all the available options
 * @param attributes any additional attributes for the element (such as class or CSS styles or size)
-->
<#macro formSingleSelectWithEmpty path options emptyValueMessage attributes="">
    <@bind path/>
    <select id="${status.expression?replace('[','')?replace(']','')}" name="${status.expression}" ${attributes}>
        <option value="">${emptyValueMessage}</option>
        <#if options?is_hash>
            <#list options?keys as value>
                <option value="${value?html}"<@checkSelected value/>>${options[value]?html}</option>
            </#list>
        <#else>
            <#list options as value>
                <option value="${value?html}"<@checkSelected value/>>${value?html}</option>
            </#list>
        </#if>
    </select>
</#macro>

