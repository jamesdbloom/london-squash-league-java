<#macro print_binding_errors objectName="">
    <#if bindingResult?? && (bindingResult.getAllErrors()?size > 0) && (bindingResult.objectName = objectName)>
    <div id="validation_error_${objectName}" class="errors_warnings">
        <p>There were problems with the data you entered:</p>
        <#list bindingResult.getAllErrors() as error>
            <p class="validation_error" style="margin-left: 2em;">&ndash; ${error.defaultMessage}</p>
        </#list>
    </div>
    <script>
        var errors = errors || {};
        errors.${objectName} = true;
    </script>
    </#if>
</#macro>

<#macro print_errors_list objectName="">
    <#if validationErrors?? && (validationErrors?size > 0)>
    <div id="validation_error_password" class="errors_warnings">
        <p>There were problems with the data you entered:</p>
        <#list validationErrors as error>
            <p class="validation_error" style="margin-left: 2em;">&ndash; ${error}</p>
        </#list>
    </div>
    <script>
        var errors = errors || {};
        errors.${objectName} = true;
    </script>
    </#if>
</#macro>