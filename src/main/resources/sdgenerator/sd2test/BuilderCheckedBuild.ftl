<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("objectName","fields", "variables", "monitorName", "assignFields", "assignVariables")}
${cd4c.method("public ${objectName}Mock build()")}

if (!isValid()) {
  <#list fields as field>
  if (${field.getName()} == null) {
    Log.error("0xA4522 ${field.getName()} must not be null.");
  }
  </#list>
  <#list variables as variable>
  if (${variable.getName()} == null) {
    Log.error("0xA4522 ${variable.getName()} must not be null.");
  }
  </#list>
  throw new IllegalStateException();
}

return uncheckedBuild();
