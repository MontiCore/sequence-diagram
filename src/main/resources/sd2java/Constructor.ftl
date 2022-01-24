${tc.signature("ctorSignature", "ctorAssignments")}
${cd4c.constructor("${ctorSignature}")}
<#list ctorAssignments as assignment>
  <#t>this.${assignment} = ${assignment};
</#list>
