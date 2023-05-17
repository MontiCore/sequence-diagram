<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("classNameList", "parentMillName")}
${cd4c.method(" public static void reset ()")}

mill = null;
<#list classNameList as attribute>
  millMock${attribute}Builder = null;
</#list>

${parentMillName}.reset();
