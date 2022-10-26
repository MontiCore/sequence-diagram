/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development.sdgenerator.sd2test;

import de.monticore.cd4code.CD4CodeMill;
import de.monticore.cd4codebasis._ast.ASTCDMethod;
import de.monticore.cdbasis._ast.ASTCDCompilationUnit;
import de.monticore.cdbasis._ast.ASTCDElement;
import de.monticore.cdinterfaceandenum._ast.ASTCDInterface;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.lang.sd4development._symboltable.ISD4DevelopmentArtifactScope;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.types.MCTypeFacade;

import java.util.List;

public class MonitorInterfaceTransformer extends AbstractVisitor {

  @Override
  public void visit(ASTSDArtifact ast) {
    ASTCDMethod getMonitorNameMethod = CD4CodeMill.cDMethodBuilder()
      .setModifier(CD4CodeMill.modifierBuilder().PUBLIC().ABSTRACT().build())
      .setMCReturnType(CD4CodeMill.mCReturnTypeBuilder()
        .setMCType(MCTypeFacade.getInstance().createStringType())
        .build())
      .setName("getMonitorName").build();

    ASTCDInterface astcdInterface = CD4CodeMill.cDInterfaceBuilder()
      .setModifier(CD4CodeMill.modifierBuilder().PUBLIC().build())
      .setName("Monitor")
      .addCDMember(getMonitorNameMethod)
      .build();

    classes.add(astcdInterface);
    compilationUnit.getCDDefinition().getCDPackagesList().get(0).addCDElement(astcdInterface);
  }

  public MonitorInterfaceTransformer(ASTCDCompilationUnit compilationUnit, List<ASTCDElement> classes, ISD4DevelopmentArtifactScope scope, GlobalExtensionManagement glex) {
    super(compilationUnit, classes, scope, glex);
  }
}
