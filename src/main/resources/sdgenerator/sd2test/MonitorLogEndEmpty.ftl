<#-- (c) https://github.com/MontiCore/monticore -->
${tc.signature("objectName","transitionName", "monitorName")}
${cd4c.method("public void logEnd${objectName}${transitionName}()")}

if(this.currentState == ${monitorName}State.RETURN_${transitionName}){
  this.currentState  = currentState.next();
  System.out.println(monitorName + ": Expected return at ${transitionName} method of ${objectName} object reached");
}
else{
  Log.error(monitorName + ": Unexpected return ${transitionName} of ${objectName} object reached");
}
