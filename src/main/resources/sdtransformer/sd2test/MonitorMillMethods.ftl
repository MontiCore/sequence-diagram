<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("methodName", "classNameList", "sdName", "parentMillName", "monitorName")}
<#if methodName == "initMe">
${cd4c.method(" public static void initMe (${sdName}Mill a)")}
    mill = a;
    <#list classNameList as attribute>
    millMock${attribute}Builder = a;
    </#list>

<#elseif methodName == "reset">
${cd4c.method(" public static void reset ()")}

    mill = null;
    <#list classNameList as attribute>
     millMock${attribute}Builder = null;
    </#list>
  ${parentMillName}.reset();

<#elseif methodName == "getMill">
${cd4c.method(" protected static ${sdName}Mill getMill ()")}

          if (mill == null) {
              mill = new ${sdName}Mill();
          }
          return mill;

<#elseif methodName == "init">
${cd4c.method(" public static void init ()")}

     mill = new ${sdName}Mill();
     monitor = new ${sdName}Monitor();
     ${parentMillName}.initMe(new ${parentMillName}ForMock());

<#elseif methodName == "getMonitor">
${cd4c.method(" public static ${sdName}Monitor get${sdName}Monitor()")}
    return monitor;
</#if>
