/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development.sdtransformer.sd2test;

import de.monticore.cd.facade.MCQualifiedNameFacade;
import de.monticore.cd.methodtemplates.CD4C;
import de.monticore.cd4code.CD4CodeMill;
import de.monticore.cd4codebasis._ast.ASTCDMethod;
import de.monticore.cdbasis._ast.*;
import de.monticore.cdinterfaceandenum._ast.ASTCDInterface;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis._visitor.SDBasisVisitor2;
import de.monticore.types.MCTypeFacade;

import java.util.Collections;
import java.util.List;

public class MonitorInterfaceVisitor extends AbstractVisitor {

  public MonitorInterfaceVisitor(List<ASTCDElement> classes, GlobalExtensionManagement glex) {
    super(classes, glex);
  }

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

    ASTCDDefinitionBuilder bCDDefinition = CD4CodeMill.cDDefinitionBuilder()
      .setModifier(CD4CodeMill.modifierBuilder().PUBLIC().build())
      .setName("MonitorInfrastructure").addCDElement(CD4CodeMill.cDPackageBuilder()
      .setMCQualifiedName(MCQualifiedNameFacade.createQualifiedName("de.monticore"))
      .addCDElement(astcdInterface)
      .build());

    classes.add(astcdInterface);

    compilationUnit = CD4CodeMill.cDCompilationUnitBuilder()
      .setCDDefinition(bCDDefinition.build())
      .build();
  }
}
