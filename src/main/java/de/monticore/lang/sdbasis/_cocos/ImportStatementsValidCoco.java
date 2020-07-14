package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.sd4development._symboltable.SD4DevelopmentArtifactScope;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.symboltable.ImportStatement;
import de.se_rwth.commons.logging.Log;

/**
 * Checks if the imports of a sequence diagram are valid, i.e.,
 * every import needs to be resolvable (as a variable or type)
 */
public class ImportStatementsValidCoco implements SDBasisASTSDArtifactCoCo {

  private static final String MESSAGE = "0xB0016: " +
          "Cannot resolve import '%s'.";

  @Override
  public void check(ASTSDArtifact node) {
    // Assume that enclosing scope is the artifact scope
    if (node.getEnclosingScope() instanceof SD4DevelopmentArtifactScope) {
      SD4DevelopmentArtifactScope scope = (SD4DevelopmentArtifactScope) node.getEnclosingScope();
      for (ImportStatement importStatement : scope.getImportList()) {
        if (!importStatement.isStar() &&
                !scope.resolveVariable(importStatement.getStatement()).isPresent() &&
                !scope.resolveType(importStatement.getStatement()).isPresent()) {
          Log.error(String.format(MESSAGE, importStatement.getStatement()), node.get_SourcePositionStart());
        }
      }
    }
  }
}
