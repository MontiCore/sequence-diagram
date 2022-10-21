package de.monticore.lang.sd4development.sdgenerator.sd2cd;

import de.monticore.cd.facade.MCQualifiedNameFacade;
import de.monticore.cd.methodtemplates.CD4C;
import de.monticore.cd4code.CD4CodeMill;
import de.monticore.cdbasis.CDBasisMill;
import de.monticore.cdbasis._ast.ASTCDClass;
import de.monticore.cdbasis._ast.ASTCDCompilationUnit;
import de.monticore.cdbasis._ast.ASTCDDefinition;
import de.monticore.cdbasis._ast.ASTCDPackage;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.lang.sd4development._ast.ASTSDCall;
import de.monticore.lang.sd4development._ast.ASTSDClass;
import de.monticore.lang.sdbasis._ast.*;
import de.monticore.lang.sdbasis._visitor.SDBasisVisitor2;
import de.monticore.types.mcbasictypes.MCBasicTypesMill;
import de.se_rwth.commons.logging.Log;

import java.util.*;
import java.util.stream.Collectors;

public class SD2CDVisitor implements SDBasisVisitor2 {

  protected ASTCDCompilationUnit cdCompilationUnit;

  protected ASTCDPackage cdPackage;

  protected ASTCDClass cdClass;
  protected final CD4C cd4C;

  protected final GlobalExtensionManagement glex;

  @Override
  public void visit(ASTSDArtifact sdArtifact) {

    ASTSequenceDiagram diagram = sdArtifact.getSequenceDiagram();

    this.cdPackage = CDBasisMill.cDPackageBuilder()
      .setMCQualifiedName(CDBasisMill.mCQualifiedNameBuilder()
        .setPartsList(List.of(diagram.getName().toLowerCase()))
        .build())
      .build();

    ASTCDDefinition cdDefinition = CD4CodeMill.cDDefinitionBuilder()
      .setName(diagram.getName())
      .setModifier(CDBasisMill.modifierBuilder().PUBLIC().build())
      .setCDElementsList(Collections.singletonList(cdPackage))
      .build();
    
    this.cdCompilationUnit = CDBasisMill.cDCompilationUnitBuilder()
      .setMCPackageDeclaration(CD4CodeMill.mCPackageDeclarationBuilder()
        .setMCQualifiedName((sdArtifact.isPresentPackageDeclaration())
          ? sdArtifact.getPackageDeclaration()
          : MCQualifiedNameFacade.createQualifiedName(diagram.getName()))
        .build())
      .setCDDefinition(cdDefinition)
      .build();
    
    this.cdClass = CDBasisMill.cDClassBuilder()
      .setModifier(CD4CodeMill.modifierBuilder().PUBLIC().build())
      .setName(diagram.getName())
      .build();

    String parameters = diagram.getSDObjectList().stream()
      .map(it -> it.getMCObjectType().printType(MCBasicTypesMill.mcBasicTypesPrettyPrinter()) + " " + it.getName())
      .collect(Collectors.joining(", "));

    List<String> assignments = diagram.getSDObjectList().stream()
      .map(ASTSDObject::getName)
      .collect(Collectors.toList());

    this.cd4C.addConstructor(cdClass, "sdgenerator.sd2java.Constructor", diagram.getName(), parameters, assignments);
    this.cd4C.addConstructor(cdClass, "sdgenerator.sd2java.DefaultConstructor", diagram.getName());


    this.cdPackage.addCDElement(cdClass);
  }

  @Override
  public void visit(ASTSDElement element) {
    if(element instanceof ASTSDSendMessage) {
      ASTSDSendMessage message = (ASTSDSendMessage) element;

      ASTSDTarget target = message.getSDTarget();
      String receiver = "";

      if(target instanceof ASTSDClass) {
        receiver = ((ASTSDClass) target).getMCObjectType().printType(MCBasicTypesMill.mcBasicTypesPrettyPrinter());
      } else if(target instanceof ASTSDObjectTarget) {
        receiver = ((ASTSDObjectTarget) target).getName();
      } else {
        Log.error("Unknown ASTSDTarget implementation: " + target.getClass().getName());
      }

      ASTSDAction action = message.getSDAction();

      if(action instanceof ASTSDCall) {
        ASTSDCall call = (ASTSDCall) action;
        String callee = call.getName();

        this.cd4C.addMethod(cdClass, "sdgenerator.sd2java.CallAction", receiver + "." + callee + "();");
      }
    }
  }

  @Override
  public void visit(ASTSDObject object) {
    String typeName = object.getMCObjectType().printType(MCBasicTypesMill.mcBasicTypesPrettyPrinter());
    String sb = "public " +
      typeName + " " +
      object.getName() +
      ";";

    this.cd4C.addAttribute(cdClass, sb);
  }

  public SD2CDVisitor(GlobalExtensionManagement glex) {
    this.glex = glex;
    this.cd4C = CD4C.getInstance();
  }

  public ASTCDCompilationUnit getCompilationUnit() {
    return cdCompilationUnit;
  }

  public ASTCDClass getCdClass() {
    return cdClass;
  }
}
