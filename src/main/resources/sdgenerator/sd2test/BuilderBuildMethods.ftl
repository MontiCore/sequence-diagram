<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("objectName","attributes", "check", "monitorName", "assignList")}
<#if check>
        ${cd4c.method("public Mock${objectName} build()")}
        if (!isValid()) {
        <#list attributes as attribute>
            if (${attribute.getName()} == null) {
                Log.error("0xA4522 ${attribute.getName()} must not be null");
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
        <#list assignList as attribute>
        value.${attribute.getName()} = this.${attribute.getName()};
        </#list>
        return value;
