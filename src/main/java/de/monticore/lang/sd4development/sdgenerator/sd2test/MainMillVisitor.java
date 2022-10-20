/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development.sdgenerator.sd2test;

import de.monticore.cd4code.CD4CodeMill;
import de.monticore.cdbasis._ast.*;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.types.MCTypeFacade;
import de.monticore.umlmodifier._ast.ASTModifierBuilder;

import java.util.List;

public class MainMillVisitor extends AbstractVisitor {

  @Override
  public void visit(ASTSDArtifact ast) {

    boolean hasMill = false;
    String millName = "";
    for(ASTCDClass astCDClass: compilationUnit.getCDDefinition().getCDClassesList()) {
      if(astCDClass.getName().endsWith("Mill")) {
        hasMill = true;
        millName = astCDClass.getName();
      }
    }
/*    if(!hasMill) {
      Log.error("0xA4387 No mill exists in the production code.");
    }
*/
    ASTCDClass mockMillClass = new ASTCDClassBuilder()
      .setModifier(new ASTModifierBuilder().PUBLIC().build())
      .setName(millName + "ForMock")
      .setCDExtendUsage(CD4CodeMill.cDExtendUsageBuilder()
        .addSuperclass(MCTypeFacade.getInstance().createQualifiedType(millName)).build())
      .build();

    for(ASTCDClass astcdClass: compilationUnit.getCDDefinition().getCDClassesList()) {
      if(astcdClass.getName().endsWith("Mill")) {
        continue;
      }
      cd4C.addMethod(mockMillClass, "sdgenerator.sd2test.MockMillMethods", astcdClass.getName(), uncapitalize(astcdClass.getName()));
    }

    classes.add(mockMillClass);

    compilationUnit.getCDDefinition().getCDPackagesList().get(0)
      .addCDElement(mockMillClass);
  }

  public MainMillVisitor(ASTCDCompilationUnit compilationUnit, List<ASTCDElement> classes, GlobalExtensionManagement glex) {
    super(compilationUnit, classes, glex);
  }
}
