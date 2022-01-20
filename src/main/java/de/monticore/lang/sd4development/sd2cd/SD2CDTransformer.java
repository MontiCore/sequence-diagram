/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development.sd2cd;

import de.monticore.cd.methodtemplates.CD4C;
import de.monticore.cd4code.CD4CodeMill;
import de.monticore.cdbasis._ast.*;
import de.monticore.lang.sd4development._ast.ASTSDCall;
import de.monticore.lang.sd4development._ast.ASTSDClass;
import de.monticore.lang.sdbasis._ast.*;
import de.monticore.types.mcbasictypes.MCBasicTypesMill;
import de.monticore.types.mcbasictypes._ast.ASTMCPackageDeclarationBuilder;
import de.monticore.umlmodifier._ast.ASTModifierBuilder;
import de.se_rwth.commons.logging.Log;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// TODO This is an incomplete early proof-of-concept for SD2CD functionality and needs further work.
public class SD2CDTransformer {

  public ASTCDCompilationUnit transform(ASTSDArtifact ast) {
    ASTSequenceDiagram sequenceDiagram = ast.getSequenceDiagram();
    ASTSDBody sdBody = sequenceDiagram.getSDBody();

    ASTCDCompilationUnitBuilder bCDCompilationUnit = CD4CodeMill.cDCompilationUnitBuilder()
      .setMCPackageDeclaration(
        new ASTMCPackageDeclarationBuilder()
          .setMCQualifiedName(ast.getPackageDeclaration())
          .build()
      );

    ASTCDClass cdClass = new ASTCDClassBuilder()
      .setModifier(new ASTModifierBuilder().PUBLIC().build())
      .setName(sequenceDiagram.getName())
      .build();

    String ctorParameters = sequenceDiagram.getSDObjectList().stream()
      .map(it -> it.getMCObjectType().printType(MCBasicTypesMill.mcBasicTypesPrettyPrinter()) + " " + it.getName())
      .collect(Collectors.joining(", "));

    String ctorSignature = "public " + sequenceDiagram.getName() + "(" + ctorParameters + ")";

    List<String> ctorAssignments = sequenceDiagram.getSDObjectList().stream()
      .map(ASTSDObject::getName)
      .collect(Collectors.toList());

    CD4C.getInstance().addConstructor(cdClass, "sd2java.Constructor", ctorSignature, ctorAssignments);

    for (ASTSDObject sdObject : sequenceDiagram.getSDObjectList()) {
      transform(sdObject, cdClass);
    }

    for (ASTSDElement sdElement : sdBody.getSDElementList()) {
      transform(sdElement, cdClass);
    }

    ASTCDDefinitionBuilder bCDDefinition = CD4CodeMill.cDDefinitionBuilder()
      .setModifier(CD4CodeMill.modifierBuilder().PUBLIC().build())
      .setName(sequenceDiagram.getName())
      .setCDElementsList(
        Collections.singletonList(
          new ASTCDPackageBuilder()
            .setMCQualifiedName(ast.getPackageDeclaration())
            .setCDElementsList(Collections.singletonList(cdClass))
            .build()
        )
      );

    bCDCompilationUnit.setCDDefinition(bCDDefinition.build());
    return bCDCompilationUnit.build();
  }

  public void transform(ASTSDElement element, ASTCDClass dest) {
    if (element instanceof ASTSDSendMessage) {
      ASTSDSendMessage sendMessage = (ASTSDSendMessage) element;
      // TODO The message source is currently ignored. See sendMessage.getSource()

      ASTSDTarget target = sendMessage.getSDTarget();
      String receiver = "";

      if (target instanceof ASTSDClass) {
        receiver = ((ASTSDClass) target).getMCObjectType().printType(MCBasicTypesMill.mcBasicTypesPrettyPrinter());
      } else if (target instanceof ASTSDObjectTarget) {
        receiver = ((ASTSDObjectTarget) target).getName();
      } else {
        Log.error("Unknown ASTSDTarget implementation: " + target.getClass().getName());
      }

      ASTSDAction action = sendMessage.getSDAction();

      if (action instanceof ASTSDCall) {
        ASTSDCall call = (ASTSDCall) action;
        String callee = call.getName();

        CD4C.getInstance().addMethod(dest, "sd2java.CallAction", receiver + "." + callee + "();");
      } else {
        // TODO This method is WIP and does not yet cover all cases.
        Log.error("Unknown ASTSDAction implementation: " + action.getClass().getName());
      }
    } else {
      // TODO This method is WIP and does not yet cover all cases.
      Log.error("Unknown ASTSDElement implementation: " + element.getClass().getName());
    }
  }

  public void transform(ASTSDObject object, ASTCDClass dest) {
    String typeName = object.getMCObjectType().printType(MCBasicTypesMill.mcBasicTypesPrettyPrinter());
    String sb = "public " +
      typeName + " " +
      object.getName() +
      ";";

    CD4C.getInstance().addAttribute(dest, sb);
  }

}
