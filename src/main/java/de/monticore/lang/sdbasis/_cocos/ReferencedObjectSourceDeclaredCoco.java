package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.sdbasis._ast.ASTSDObjectSource;
import de.se_rwth.commons.logging.Log;

public class ReferencedObjectSourceDeclaredCoco implements SDBasisASTSDObjectSourceCoCo {

  static final String MESSAGE = ReferencedObjectSourceDeclaredCoco.class.getSimpleName() + ": " +
          "Object '%s' is used, but not declared.";

  @Override
  public void check(ASTSDObjectSource node) {
    if (!node.getEnclosingScope().resolveVariable(node.getName()).isPresent()) {
      Log.error(String.format(MESSAGE, node.getName()), node.get_SourcePositionStart());
    }
  }
}
