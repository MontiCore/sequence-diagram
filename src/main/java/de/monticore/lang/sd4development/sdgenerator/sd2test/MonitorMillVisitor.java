/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development.sdgenerator.sd2test;

import de.monticore.cd4code.CD4CodeMill;
import de.monticore.cdbasis._ast.*;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis._ast.ASTSequenceDiagram;
import de.monticore.types.MCTypeFacade;

import java.util.ArrayList;
import java.util.List;

public class MonitorMillVisitor extends AbstractVisitor {

  public MonitorMillVisitor(ASTCDCompilationUnit compilationUnit, List<ASTCDElement> classes, GlobalExtensionManagement glex) {
    super(compilationUnit, classes, glex);
  }

  @Override
  public void visit(ASTSDArtifact ast) {

    ASTSequenceDiagram sequenceDiagram = ast.getSequenceDiagram();
    String millName = sequenceDiagram.getName() + "Mill";
    String mainMillName = "";

    ASTCDClass cdClass = CD4CodeMill.cDClassBuilder()
      .setModifier(CD4CodeMill.modifierBuilder().PUBLIC().build())
      .setName(millName)
      .build();

    List<String> classList = new ArrayList<>();
    for (ASTCDElement cdElement : compilationUnit.getCDDefinition().getCDPackagesList().get(0).getCDElementList()) {
      ASTCDClass astCDClass;
      if(cdElement instanceof ASTCDClass) {
        astCDClass = (ASTCDClass) cdElement;
      } else {
       continue;
      }
      if (astCDClass.getName().endsWith("Mill")) {
        mainMillName = astCDClass.getName() ;
        continue;
      }
      classList.add(astCDClass.getName());
      String millAttribute = "protected static " + millName + " millMock" + astCDClass.getName() + "Builder;";
      cd4C.addAttribute(cdClass, millAttribute);
    }

    cdClass.setCDExtendUsage(CD4CodeMill.cDExtendUsageBuilder()
      .addSuperclass(MCTypeFacade.getInstance().createQualifiedType(mainMillName+"ForMock"))
      .build());

    String millNameAttribute = "protected static " + millName + " mill;";
    cd4C.addAttribute(cdClass, millNameAttribute);
    String monitorAttribute = "protected static " + sequenceDiagram.getName() + "Monitor monitor;";
    cd4C.addAttribute(cdClass, monitorAttribute);

    cd4C.addConstructor(cdClass, "sdgenerator.sd2java.DefaultConstructor", sequenceDiagram.getName()+"Mill");

    cd4C.addMethod(cdClass, "sdgenerator.sd2test.MonitorMillMethods",
      "init",classList, sequenceDiagram.getName(), mainMillName, uncapitalize(sequenceDiagram.getName()));

    cd4C.addMethod(cdClass, "sdgenerator.sd2test.MonitorMillMethods",
      "initMe",classList, sequenceDiagram.getName(), mainMillName, uncapitalize(sequenceDiagram.getName()));

    cd4C.addMethod(cdClass, "sdgenerator.sd2test.MonitorMillMethods",
      "getMill",classList, sequenceDiagram.getName(), mainMillName, uncapitalize(sequenceDiagram.getName()));

    cd4C.addMethod(cdClass, "sdgenerator.sd2test.MonitorMillMethods",
      "getMonitor",classList, sequenceDiagram.getName(), mainMillName, uncapitalize(sequenceDiagram.getName()));

    cd4C.addMethod(cdClass, "sdgenerator.sd2test.MonitorMillMethods",
      "reset",classList, sequenceDiagram.getName(), mainMillName, uncapitalize(sequenceDiagram.getName()));

    compilationUnit.getCDDefinition().getCDPackagesList().get(0)
      .addCDElement(cdClass);
  }
}
