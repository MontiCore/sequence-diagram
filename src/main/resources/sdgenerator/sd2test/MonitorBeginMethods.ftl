<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("objectName","transitionName", "methodParameters" ,"parameterList", "valueList", "monitorName")}
${cd4c.method("public void logBegin${objectName}${transitionName}(${cdPrinter.printCDParametersDecl(methodParameters)})")}
EqualityChecker e = new EqualityChecker();
if(this.currentState == ${monitorName}State.CALL_${transitionName}) {
this.currentState = currentState.next();
<#list parameterList as param>
  if (!e.isEqual(${param.getParameterName().getName()},${valueList[param?index]})) {
  Log.error(monitorName + ": Incorrect parameter value(s) at ${transitionName} of ${objectName} object found ");
  }
</#list>
  System.out.println(monitorName + ": Expected transition ${transitionName} of ${objectName} found");
}
else {
Log.error(monitorName + ": Incorrect transition ${transitionName} of ${objectName} object reached");
}
