<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("objectName","transitionName", "returnValue", "returnType", "monitorName")}
${cd4c.method("public void logEnd${objectName}${transitionName}(${returnType} returnValue)")}

if(this.currentState == ${monitorName}State.RETURN_${transitionName}) {
  this.currentState  = currentState.next();
  if(returnValue == ${returnValue}) {
    System.out.println(monitorName + ":  Return at ${transitionName} method of ${objectName} object reached with expected return value");
  }
  else {
    Log.error(monitorName + ": Incorrect return value and return at ${transitionName} method of ${objectName} object reached");
  }
}
else {
  Log.error(monitorName + ": Unexpected return ${transitionName} of ${objectName} object reached");
}
