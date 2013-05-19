<#macro print_errors objectName="">
    <#if bindingResult?? && (bindingResult.getAllErrors()?size > 0) && (bindingResult.objectName = objectName)>
    <div id="validation_error_${objectName}" class="errors" style="color: red; margin: 1em; border: 1px; border-color: red; border-style: dashed; width: 82%;">
        <p>There were problems with the data you entered:</p>
        <#list bindingResult.getAllErrors() as error>
            <p class="validation_error" style="margin-left: 2em;">${error.defaultMessage}</p>
        </#list>
    </div>
    </#if>
</#macro>