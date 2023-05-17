/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development.sdgenerator;

import de.monticore.cd4code.CD4CodeMill;
import de.monticore.cdbasis._ast.*;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.lang.sd4development._symboltable.ISD4DevelopmentArtifactScope;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis._ast.ASTSequenceDiagram;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.symbols.oosymbols._symboltable.OOTypeSymbol;
import de.monticore.types.MCTypeFacade;

import java.util.ArrayList;
import java.util.List;

public class MonitorMillTransformer extends AbstractVisitor {

  @Override
  public void visit(ASTSDArtifact ast) {

    ASTSequenceDiagram sequenceDiagram = ast.getSequenceDiagram();
    String millName = capitalize(sequenceDiagram.getName() + "Mill");
    String mainMillName = "";

    ASTCDClass cdClass = CD4CodeMill.cDClassBuilder()
      .setModifier(CD4CodeMill.modifierBuilder().PUBLIC().build())
      .setName(capitalize(millName))
      .build();

    List<String> classList = new ArrayList<>();
    for (TypeSymbol type : scope.getTypeSymbols().values()) {
      if (type.getName().endsWith("Mill")) {
        mainMillName = type.getName() ;
        continue;
      }
      classList.add(type.getName());
      String millAttribute = "protected static " + millName + " millMock" + type.getName() + "Builder;";
      cd4C.addAttribute(cdClass, millAttribute);
    }
    for (OOTypeSymbol type : scope.getOOTypeSymbols().values()) {
      if (type.getName().endsWith("Mill")) {
        mainMillName = type.getName() ;
        continue;
      }
      classList.add(type.getName());
      String millAttribute = "protected static " + millName + " millMock" + type.getName() + "Builder;";
      cd4C.addAttribute(cdClass, millAttribute);
    }

    cdClass.setCDExtendUsage(CD4CodeMill.cDExtendUsageBuilder()
      .addSuperclass(MCTypeFacade.getInstance().createQualifiedType(mainMillName+"ForMock"))
      .build());

    String millNameAttribute = "protected static " + millName + " mill;";
    cd4C.addAttribute(cdClass, millNameAttribute);

    String diagramName = capitalize(sequenceDiagram.getName());
    String monitorAttribute = "protected static " + diagramName + "Monitor monitor;";
    cd4C.addAttribute(cdClass, monitorAttribute);

    cd4C.addConstructor(cdClass, "sdgenerator.sd2java.DefaultConstructor", sequenceDiagram.getName()+"Mill");

    cd4C.addMethod(cdClass, "sdgenerator.sd2test.MillInit",
      diagramName, mainMillName);

    cd4C.addMethod(cdClass, "sdgenerator.sd2test.MillInitMe",
      classList, diagramName);

    cd4C.addMethod(cdClass, "sdgenerator.sd2test.MillGetMill",
      diagramName);

    cd4C.addMethod(cdClass, "sdgenerator.sd2test.MillGetMonitor",
      diagramName);

    cd4C.addMethod(cdClass, "sdgenerator.sd2test.MillReset",
      classList, mainMillName);

    compilationUnit.getCDDefinition().getCDPackagesList().get(0).addCDElement(cdClass);
  }

  public MonitorMillTransformer(ASTCDCompilationUnit compilationUnit, List<ASTCDElement> classes, ISD4DevelopmentArtifactScope scope, GlobalExtensionManagement glex) {
    super(compilationUnit, classes, scope, glex);
  }
}
