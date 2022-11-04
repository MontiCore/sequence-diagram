${tc.signature("objectName","fields", "variables", "check", "monitorName", "assignFields", "assignVariables")}
<#if check>
    ${cd4c.method("public Mock${objectName} build()")}
  if (!isValid()) {
    <#list fields as field>
      if (${field.getName()} == null) {
      Log.error("0xA4522 ${field.getName()} must not be null");
      }
    </#list>
    <#list variables as variable>
      if (${variable.getName()} == null) {
      Log.error("0xA4522 ${variable.getName()} must not be null");
      }
    </#list>
  throw new IllegalStateException();
  }
<#else>
    ${cd4c.method("public Mock${objectName} uncheckedBuild()")}
</#if>

Mock${objectName} value;
value = new Mock${objectName}();
value.set${monitorName}Monitor(${monitorName}Mill.get${monitorName}Monitor());
<#list assignFields as field>
  value.${field.getName()} = this.${field.getName()};
</#list>
<#list assignVariables as variable>
  value.${variable.getName()} = this.${variable.getName()};
</#list>
return value;
