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

<#--
 * checkSelectedIfMatch
 *
 * Check a value in a list to see if it is the currently selected value.
 * If so, add the 'selected="selected"' text to the output.
 * Handles values of numeric and string types.
 * This function is used internally but can be accessed by user code if required.
 *
 * @param value the current value in a list iteration
-->
<#macro checkSelectedIfMatch optionId value>
    <!-- ${optionId?is_number?string} && ${optionId} == ${value?number} -->
    <!-- ${optionId?is_string?string} && ${optionId} == ${value} -->
    <#if optionId?is_number && optionId == value?number>selected="selected"</#if>
    <#if optionId?is_string && optionId == value>selected="selected"</#if>
</#macro>

