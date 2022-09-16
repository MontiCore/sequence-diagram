/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development.sd2cd;

import de.monticore.cd4code.CD4CodeMill;
import de.monticore.generating.templateengine.GlobalExtensionManagement;
import de.monticore.lang.sdbasis.SDBasisMill;
import de.monticore.lang.sdbasis._ast.*;
import de.monticore.lang.sdbasis._visitor.SDBasisTraverser;

// TODO This is an incomplete early proof-of-concept for SD2CD functionality and needs further work.
public class SD2CDTransformer {

  public SC2CDData transform(ASTSDArtifact ast, GlobalExtensionManagement glex) {
    SC2CDVisitor visitor = new SC2CDVisitor(glex);
    SDBasisTraverser traverser = SDBasisMill.inheritanceTraverser();
    traverser.add4SDBasis(visitor);

    CD4CodeMill.init();

    traverser.handle(ast);

    return new SC2CDData(visitor.getCompilationUnit(),
      visitor.getCdClass(),
      visitor.getClassMap().values());
  }
}
