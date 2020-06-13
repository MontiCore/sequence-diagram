package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis._symboltable.SDBasisArtifactScope;
import de.monticore.symboltable.ImportStatement;
import de.se_rwth.commons.logging.Log;

public class ImportStatementsValidCoco implements SDBasisASTSDArtifactCoCo {

  static final String MESSAGE = ImportStatementsValidCoco.class.getSimpleName() + ": " +
          "Cannot resolve import '%s'.";

  @Override
  public void check(ASTSDArtifact node) {
    // Assume that enclosing scope is the artifact scope
    if (node.getEnclosingScope() instanceof SDBasisArtifactScope) {
      SDBasisArtifactScope scope = (SDBasisArtifactScope) node.getEnclosingScope();
      for (ImportStatement importStatement : scope.getImportList()) {
        if (!importStatement.isStar() && !scope.resolveType(importStatement.getStatement()).isPresent()) {
          Log.error(String.format(MESSAGE, importStatement.getStatement()), node.get_SourcePositionStart());
        }
      }
    }
  }
}
