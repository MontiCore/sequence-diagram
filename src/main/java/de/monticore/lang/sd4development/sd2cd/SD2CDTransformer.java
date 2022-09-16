/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development.sd2cd;

import de.monticore.cd4code.CD4CodeMill;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sd4development.SD4DevelopmentTool;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentTraverser;
import de.monticore.lang.sdbasis.SDBasisMill;
import de.monticore.lang.sdbasis._ast.*;
import de.monticore.lang.sdbasis._visitor.SDBasisTraverser;

public class SD2CDTransformer {

  public SC2CDData transform(ASTSDArtifact ast, GlobalExtensionManagement glex) {
    SC2CDVisitor visitor = new SC2CDVisitor(glex);
    SD4DevelopmentTraverser traverser = SD4DevelopmentMill.inheritanceTraverser();
    traverser.add4SDBasis(visitor);

    CD4CodeMill.init();

    traverser.handle(ast);

    return new SC2CDData(visitor.getCompilationUnit(),
      visitor.getCdClass(),
      visitor.getClassMap().values());
  }
}
