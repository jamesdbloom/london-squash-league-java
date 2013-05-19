<#macro print_errors objectName="">
    <#if bindingResult?? && (bindingResult.getAllErrors()?size > 0) && (bindingResult.objectName = objectName)>
    <!-- style="color: red; margin: 1em; border: 1px; border-color: red; border-style: dashed; width: 82%;" -->
    <div id="validation_error_${objectName}" class="errors_warnings">
        <p>There were problems with the data you entered:</p>
        <#list bindingResult.getAllErrors() as error>
            <p class="validation_error" style="margin-left: 2em;">&ndash; ${error.defaultMessage}</p>
        </#list>
    </div>
    </#if>
</#macro>