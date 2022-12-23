<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("classNameList", "sdName")}
${cd4c.method(" public static void initMe (${sdName}Mill a)")}

mill = a;
<#list classNameList as attribute>
  millMock${attribute}Builder = a;
</#list>
