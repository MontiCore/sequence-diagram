<#-- (c) https://github.com/MontiCore/monticore -->
<#--
  This config template does the default SD2Java generation.

  Call it using the CLI: .. -ct sd2java.SD2Java

-->
${tc.signature("glex", "sdTransformer", "cdGenerator", "scope")}

<#if scope??>
    <#assign cddata = sdTransformer.transform(ast, scope, glex)>
<#else>
    <#assign cddata = sdTransformer.transform(ast, glex)>
</#if>

${cdGenerator.generate(cddata.getCompilationUnit())}
