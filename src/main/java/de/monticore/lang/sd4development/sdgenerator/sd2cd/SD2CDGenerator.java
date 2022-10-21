/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development.sdgenerator.sd2cd;

import de.monticore.cd4code.CD4CodeMill;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sd4development._symboltable.ISD4DevelopmentArtifactScope;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentTraverser;
import de.monticore.lang.sd4development.sdgenerator.SDGenerator;
import de.monticore.lang.sdbasis._ast.*;

public class SD2CDGenerator extends SDGenerator {

  @Override
  public SD2CDData transform(ASTSDArtifact ast, GlobalExtensionManagement glex) {
    SD2CDVisitor visitor = new SD2CDVisitor(glex);
    SD4DevelopmentTraverser traverser = SD4DevelopmentMill.inheritanceTraverser();
    traverser.add4SDBasis(visitor);

    CD4CodeMill.init();

    traverser.handle(ast);

    return new SD2CDData(visitor.getCompilationUnit(),
      visitor.getCdClass());
  }

  @Override
  public SD2CDData transform(ASTSDArtifact ast, ISD4DevelopmentArtifactScope scope, GlobalExtensionManagement glex) {
    return new SD2CDData(null, null);
  }
}
