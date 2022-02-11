<#-- (c) https://github.com/MontiCore/monticore -->
<#--
  This config template does the default SD2Java generation.

  Call it using the CLI: .. -ct sd2java.SD2Java

-->
${tc.signature("glex", "sd2cdTransformer", "cdGenerator")}

${cdGenerator.generate(sd2cdTransformer.transform(ast))}
