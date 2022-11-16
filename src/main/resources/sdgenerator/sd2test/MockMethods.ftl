<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("returnType","methodName", "parameterList", "parameterString", "monitorName","targetObject", "capMethodName", "sdFlag")}
${cd4c.method("public ${returnType} ${methodName}(${cdPrinter.printCDParametersDecl(parameterList)})")}

<#if sdFlag>
    ${monitorName}.logBegin${targetObject}${capMethodName}(${parameterString});
</#if>

<#if returnType == "void">
  super.${methodName}(${parameterString});
  <#if sdFlag>
    ${monitorName}.logEnd${targetObject}${capMethodName}();
  </#if>
<#else>
  ${returnType} value = super.${methodName}(${parameterString});
  <#if sdFlag>
    ${monitorName}.logEnd${targetObject}${capMethodName}(value);
  </#if>
  return value;
</#if>
