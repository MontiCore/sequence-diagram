/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development.sdgenerator.sd2test;

import de.monticore.cd4code.CD4CodeMill;
import de.monticore.cdbasis._ast.*;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.lang.sd4development._symboltable.ISD4DevelopmentArtifactScope;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.types.MCTypeFacade;
import de.monticore.types.mcbasictypes._ast.ASTMCPrimitiveType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MockBuilderTransformer extends AbstractVisitor {

  @Override
  public void visit(ASTSDArtifact astsdArtifact) {
    List<ASTCDElement> mockBuilders = new ArrayList<>();

    for(String type : concat(scope.getTypeSymbols().keySet(), scope.getOOTypeSymbols().keySet())) {

      if(type.endsWith("Mill") ||type.endsWith("Builder")) {
        continue;
      }
      mockBuilders.add(createMockBuilders(astsdArtifact, cdClass));
    }

    classes.addAll(mockBuilders);
    compilationUnit.getCDDefinition().getCDPackagesList().get(0).addAllCDElements(mockBuilders);
  }

  private ASTCDClass createMockBuilders(ASTSDArtifact astsdArtifact, String typeName) {

    String builderType = "Mock" + typeName + "Builder";

    String sdName = astsdArtifact.getSequenceDiagram().getName();

    ASTCDClass mockBuilder = CD4CodeMill.cDClassBuilder()
      .setModifier(CD4CodeMill.modifierBuilder().PUBLIC().build())
      .setName(builderType)
      .setCDExtendUsage(CD4CodeMill.cDExtendUsageBuilder()
        .addSuperclass(MCTypeFacade.getInstance()
          .createQualifiedType(typeName + "Builder"))
        .build())
      .build();

    String sb = "protected " + builderType + " " + "realBuilder ;";
    cd4C.addAttribute(mockBuilder, sb);

    List<ASTCDAttribute> attributeList = prodClass.getCDAttributeList().stream()
      .filter(a -> !(a.getMCType() instanceof ASTMCPrimitiveType))
      .collect(Collectors.toList());
    List<ASTCDAttribute> assignAttributeList = prodClass.getCDAttributeList();

    cd4C.addConstructor(mockBuilder, "sdgenerator.sd2test.BuilderConstructor",
      builderType);
    cd4C.addMethod(mockBuilder, "sdgenerator.sd2test.BuilderBuildMethods",
      typeName, attributeList, false, sdName, assignAttributeList);
    cd4C.addMethod(mockBuilder, "sdgenerator.sd2test.BuilderBuildMethods",
      typeName, attributeList, true, sdName, assignAttributeList);

    return mockBuilder;
  }

  public MockBuilderTransformer(ASTCDCompilationUnit compilationUnit, List<ASTCDElement> classes, ISD4DevelopmentArtifactScope scope, GlobalExtensionManagement glex) {
    super(compilationUnit, classes, scope, glex);
  }
}
