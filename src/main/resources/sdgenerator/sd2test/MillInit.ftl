<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("sdName", "parentMillName")}
${cd4c.method(" public static void init ()")}

mill = new ${sdName}Mill();
monitor = new ${sdName}Monitor();
${parentMillName}.initMe(new ${parentMillName}ForMock());
