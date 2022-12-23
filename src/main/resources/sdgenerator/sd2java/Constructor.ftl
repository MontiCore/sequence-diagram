<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("name", "parameters", "assignments")}
${cd4c.constructor("public ${name?cap_first}(${parameters})")}
<#list assignments as assignment>
  <#t>this.${assignment} = ${assignment};
</#list>
