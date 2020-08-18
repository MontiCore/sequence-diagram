package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.sdbasis._ast.ASTSDObjectTarget;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;

/**
 * Checks if used target objects are declared before they are used.
 */
public class ReferencedObjectTargetDeclaredCoco implements SDBasisASTSDObjectTargetCoCo {

  private static final String MESSAGE_NOT_DECLARED = "0xB0020: " +
          "Object '%s' is used, but not declared.";

  private static final String MESSAGE_NOT_DECLARED_BEFORE = "0xB0027: " +
    "Object '%s' is used before it is declared.";

  @Override
  public void check(ASTSDObjectTarget node) {
    Optional<VariableSymbol> varOpt = node.getEnclosingScope().resolveVariable(node.getName());
    if (!varOpt.isPresent()) {
      Log.error(String.format(MESSAGE_NOT_DECLARED, node.getName()), node.get_SourcePositionStart());
      return;
    }
    if (varOpt.get().isPresentAstNode() && varOpt.get().getAstNode().get_SourcePositionEnd().compareTo(node.get_SourcePositionStart()) > 0) {
      Log.error(String.format(MESSAGE_NOT_DECLARED_BEFORE, node.getName()), node.get_SourcePositionStart());
    }
  }
}
