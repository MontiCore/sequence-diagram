/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development.sdtransformer.sd2test;

import de.monticore.cd.methodtemplates.CD4C;
import de.monticore.cd4code.CD4CodeMill;
import de.monticore.cdbasis._ast.*;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis._ast.ASTSDBody;
import de.monticore.lang.sdbasis._visitor.SDBasisVisitor2;
import de.monticore.types.MCTypeFacade;
import de.monticore.types.mcbasictypes._ast.ASTMCPrimitiveType;
import de.monticore.umlmodifier._ast.ASTModifierBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MockBuilderTransformer extends AbstractVisitor implements SDBasisVisitor2 {

  public MockBuilderTransformer(ASTCDCompilationUnit compilationUnit, List<ASTCDElement> classes, GlobalExtensionManagement glex) {
    super(compilationUnit, classes, glex);
  }

  @Override
  public void visit(ASTSDArtifact astsdArtifact) {
    List<ASTCDElement> mockBuilders = new ArrayList<>();

    for(ASTCDElement cdElement : compilationUnit.getCDDefinition()
      .getCDPackagesList().get(0).getCDElementList()) {

      ASTCDClass cdClass;
      if(cdElement instanceof ASTCDClass) {
        cdClass = (ASTCDClass) cdElement;
      } else {
        continue;
      }

      if(cdClass.getName().endsWith("Mill") || cdClass.getName().endsWith("Builder")) {
        continue;
      }
      mockBuilders.add(createMockBuilders(astsdArtifact, cdClass));
    }

    classes.addAll(mockBuilders);
    compilationUnit.getCDDefinition().getCDPackagesList().get(0).addAllCDElements(mockBuilders);
  }

  private ASTCDClass createMockBuilders(ASTSDArtifact astsdArtifact, ASTCDClass prodClass) {

    String builderType = "Mock" + prodClass.getName() + "Builder";

    String sdName = astsdArtifact.getSequenceDiagram().getName();

    ASTCDClass mockBuilder = CD4CodeMill.cDClassBuilder()
      .setModifier(CD4CodeMill.modifierBuilder().PUBLIC().build())
      .setName(builderType)
      .setCDExtendUsage(CD4CodeMill.cDExtendUsageBuilder()
        .addSuperclass(MCTypeFacade.getInstance()
          .createQualifiedType(prodClass.getName() + "Builder"))
        .build())
      .build();

    String sb = "protected " + builderType + " " + "realBuilder ;";
    cd4C.addAttribute(mockBuilder, sb);

    List<ASTCDAttribute> attributeList = prodClass.getCDAttributeList().stream()
      .filter(a -> !(a.getMCType() instanceof ASTMCPrimitiveType))
      .collect(Collectors.toList());
    List<ASTCDAttribute> assignAttributeList = prodClass.getCDAttributeList();

    cd4C.addConstructor(mockBuilder, "sdtransformer.sd2test.BuilderConstructor",
      builderType);
    cd4C.addMethod(mockBuilder, "sdtransformer.sd2test.BuilderBuildMethods",
      prodClass.getName(), attributeList, false, sdName, assignAttributeList);
    cd4C.addMethod(mockBuilder, "sdtransformer.sd2test.BuilderBuildMethods",
      prodClass.getName(), attributeList, true, sdName, assignAttributeList);

    return mockBuilder;
  }

}
