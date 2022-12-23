<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("sdName")}
${cd4c.method(" protected static ${sdName}Mill getMill ()")}

if (mill == null) {
  mill = new ${sdName}Mill();
}
return mill;
