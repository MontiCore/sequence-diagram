package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.sdbasis._ast.ASTSDObjectSource;
import de.monticore.types.basictypesymbols._symboltable.VariableSymbol;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;

public class ReferencedObjectSourceDeclaredCoco implements SDBasisASTSDObjectSourceCoCo {

  private static final String MESSAGE_NOT_DECLARED = "0xS0019: " +
    "Object '%s' is used, but not declared.";

  private static final String MESSAGE_NOT_DECLARED_BEFORE = "0xS0026: " +
    "Object '%s' is used before its declaration.";

  @Override
  public void check(ASTSDObjectSource node) {
    Optional<VariableSymbol> varOpt = node.getEnclosingScope().resolveVariable(node.getName());
    if (!varOpt.isPresent()) {
      Log.error(String.format(MESSAGE_NOT_DECLARED, node.getName()), node.get_SourcePositionStart());
    } else if (varOpt.get().getAstNode().get_SourcePositionEnd().compareTo(node.get_SourcePositionStart()) > 0) {
      Log.error(String.format(MESSAGE_NOT_DECLARED_BEFORE, node.getName()), node.get_SourcePositionStart());
    }
  }
}
