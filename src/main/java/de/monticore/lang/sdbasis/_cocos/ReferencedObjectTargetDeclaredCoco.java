package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.sdbasis._ast.ASTSDObjectTarget;
import de.se_rwth.commons.logging.Log;

public class ReferencedObjectTargetDeclaredCoco implements SDBasisASTSDObjectTargetCoCo {

  static final String MESSAGE = ReferencedObjectTargetDeclaredCoco.class.getSimpleName() + ": " +
          "Object '%s' is used, but not declared.";

  @Override
  public void check(ASTSDObjectTarget node) {
    if (!node.getEnclosingScope().resolveVariable(node.getName()).isPresent()) {
      Log.error(String.format(MESSAGE, node.getName()), node.get_SourcePositionStart());
    }
  }
}
