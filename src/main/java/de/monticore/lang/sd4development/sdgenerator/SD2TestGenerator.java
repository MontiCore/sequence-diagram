/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development.sdgenerator;

import de.monticore.cd.facade.MCQualifiedNameFacade;
import de.monticore.cd4code.CD4CodeMill;
import de.monticore.cdbasis._ast.ASTCDCompilationUnit;
import de.monticore.cdbasis._ast.ASTCDDefinition;
import de.monticore.cdbasis._ast.ASTCDElement;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sd4development._symboltable.ISD4DevelopmentArtifactScope;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentTraverser;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import org.antlr.v4.runtime.misc.Pair;

import java.util.ArrayList;
import java.util.List;

public class SD2TestGenerator {

  public SD2TestData transform(ASTSDArtifact ast, ISD4DevelopmentArtifactScope scope, GlobalExtensionManagement glex) {

    ASTCDCompilationUnit compilationUnit = createCU(ast);
    List<ASTCDElement> classes = new ArrayList<>();

    Pair<ASTCDCompilationUnit, List<ASTCDElement>> p = addVisitor(ast, new MainMillTransformer(compilationUnit, classes, scope, glex));

    p = addVisitor(ast, new MockBuilderTransformer(p.a, p.b, scope, glex));

    p = addVisitor(ast, new MockClassTransformer(p.a, p.b, scope, glex));

    p = addVisitor(ast, new MonitorMillTransformer(p.a, p.b, scope, glex));

    p = addVisitor(ast, new MonitorTransformer(p.a, p.b, scope, glex));

    return new SD2TestData(p.a, p.b);
  }

  private Pair<ASTCDCompilationUnit, List<ASTCDElement>> addVisitor(ASTSDArtifact ast,
                                                                    AbstractVisitor visitor) {
    SD4DevelopmentTraverser traverser = SD4DevelopmentMill.inheritanceTraverser();
    traverser.add4SDBasis(visitor);
    traverser.handle(ast);
    return new Pair<>(visitor.getCompilationUnit(), visitor.getClasses());
  }

  private ASTCDCompilationUnit createCU(ASTSDArtifact ast) {
    ASTCDDefinition cDDefinition = CD4CodeMill.cDDefinitionBuilder()
      .setModifier(CD4CodeMill.modifierBuilder().PUBLIC().build())
      .setName(ast.getSequenceDiagram().getName())
      .addCDElement(CD4CodeMill.cDPackageBuilder()
        .setMCQualifiedName(MCQualifiedNameFacade.createQualifiedName("de.monticore"))
        .build())
      .build();

    return CD4CodeMill.cDCompilationUnitBuilder()
      .setCDDefinition(cDDefinition)
      .build();
  }
}
