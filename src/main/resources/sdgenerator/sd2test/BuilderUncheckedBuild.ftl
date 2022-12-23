<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("objectName","fields", "variables", "monitorName", "assignFields", "assignVariables")}
${cd4c.method("public ${objectName}Mock uncheckedBuild()")}

${objectName}Mock value;
value = new ${objectName}Mock();
value.set${monitorName}Monitor(${monitorName}Mill.get${monitorName}Monitor());
<#list assignFields as field>
  value.${field.getName()} = this.${field.getName()};
</#list>
<#list assignVariables as variable>
  value.${variable.getName()} = this.${variable.getName()};
</#list>
return value;



