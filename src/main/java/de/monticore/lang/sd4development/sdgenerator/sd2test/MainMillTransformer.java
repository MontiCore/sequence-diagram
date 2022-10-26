/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development.sdgenerator.sd2test;

import de.monticore.cd4code.CD4CodeMill;
import de.monticore.cdbasis._ast.*;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.lang.sd4development._symboltable.ISD4DevelopmentArtifactScope;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.symbols.oosymbols._symboltable.OOTypeSymbol;
import de.monticore.types.MCTypeFacade;
import de.monticore.umlmodifier._ast.ASTModifierBuilder;
import de.se_rwth.commons.logging.Log;

import java.util.List;

public class MainMillTransformer extends AbstractVisitor {

  @Override
  public void visit(ASTSDArtifact ast) {
    boolean hasMill = false;
    String millName = "";
    for (TypeSymbol type : scope.getTypeSymbols().values()) {
      if(type.getName().endsWith("Mill")) {
        hasMill = true;
        millName = type.getName();
      }
    }
    for (OOTypeSymbol type : scope.getOOTypeSymbols().values()) {
      if(type.getName().endsWith("Mill")) {
        hasMill = true;
        millName = type.getName();
      }
    }
    if(!hasMill) {
      Log.error("0xA4387 No mill exists in the production code.");
    }

    ASTCDClass mockMillClass = new ASTCDClassBuilder()
      .setModifier(new ASTModifierBuilder().PUBLIC().build())
      .setName(millName + "ForMock")
      .setCDExtendUsage(CD4CodeMill.cDExtendUsageBuilder()
        .addSuperclass(MCTypeFacade.getInstance().createQualifiedType(millName)).build())
      .build();

    for(TypeSymbol type : scope.getTypeSymbols().values()) {
      if(type.getName().endsWith("Mill")) {
        continue;
      }
      cd4C.addMethod(mockMillClass, "sdgenerator.sd2test.MockMillMethods", type.getName(), uncapitalize(type.getName()));
    }

    for(OOTypeSymbol type : scope.getOOTypeSymbols().values()) {
      if(type.getName().endsWith("Mill")) {
        continue;
      }
      cd4C.addMethod(mockMillClass, "sdgenerator.sd2test.MockMillMethods", type.getName(), uncapitalize(type.getName()));
    }

    classes.add(mockMillClass);
    compilationUnit.getCDDefinition().getCDPackagesList().get(0)
      .addCDElement(mockMillClass);
  }

  public MainMillTransformer(ASTCDCompilationUnit compilationUnit, List<ASTCDElement> classes, ISD4DevelopmentArtifactScope scope, GlobalExtensionManagement glex) {
    super(compilationUnit, classes, scope, glex);
  }

}
