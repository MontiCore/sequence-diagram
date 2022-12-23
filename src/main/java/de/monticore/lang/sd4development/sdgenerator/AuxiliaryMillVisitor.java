/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development.sdgenerator;

import de.monticore.cd.facade.CDExtendUsageFacade;
import de.monticore.cd4code.CD4CodeMill;
import de.monticore.cd4codebasis.CD4CodeBasisMill;
import de.monticore.cdbasis._ast.ASTCDClass;
import de.monticore.cdbasis._ast.ASTCDCompilationUnit;
import de.monticore.cdbasis._ast.ASTCDElement;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.lang.sd4development._symboltable.ISD4DevelopmentArtifactScope;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentVisitor2;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.symbols.oosymbols._symboltable.OOTypeSymbol;

import java.util.ArrayList;
import java.util.List;

public class AuxiliaryMillVisitor extends AbstractVisitor implements SD4DevelopmentVisitor2 {

  @Override
  public void visit(ASTSDArtifact sdArtifact) {

    List<ASTCDClass> auxiliaryMills = new ArrayList<>();

    for(TypeSymbol type : scope.getTypeSymbols().values()) {
      if(type.getName().contains("MillFor")) {
        auxiliaryMills.add(createAuxMill(type, sdArtifact));
      }
    }

    for(OOTypeSymbol type : scope.getOOTypeSymbols().values()) {
      if(type.getName().contains("MillFor")) {
        auxiliaryMills.add(createAuxMill(type, sdArtifact));
      }
    }

    classes.addAll(auxiliaryMills);
    compilationUnit.getCDDefinition().getCDPackagesList().get(0).addAllCDElements(auxiliaryMills);
  }

  private ASTCDClass createAuxMill(TypeSymbol type, ASTSDArtifact sdArtifact) {

    String artifactName = capitalize(sdArtifact.getSequenceDiagram().getName());
    String millName = type.getName().substring(0, type.getName().indexOf("MillFor"));

    ASTCDClass cdClass = CD4CodeMill.cDClassBuilder()
      .setModifier(CD4CodeMill.modifierBuilder()
        .PUBLIC()
        .build())
      .setName(millName + "MillFor"+artifactName)
      .setCDExtendUsage(CDExtendUsageFacade.getInstance().createCDExtendUsage(millName + "Mill"))
      .build();

    return cdClass;
  }

  public AuxiliaryMillVisitor(ASTCDCompilationUnit compilationUnit, List<ASTCDElement> classes, ISD4DevelopmentArtifactScope scope, GlobalExtensionManagement glex) {
    super(compilationUnit, classes, scope, glex);
  }
}
