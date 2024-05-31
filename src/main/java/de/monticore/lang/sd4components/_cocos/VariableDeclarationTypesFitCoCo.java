/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._cocos;

import de.monticore.lang.sd4components._ast.ASTSDVariableDeclaration;
import de.monticore.lang.sd4components.types.FullSD4ComponentsDeriver;
import de.monticore.types.check.AbstractDerive;
import de.monticore.types.check.TypeCheckResult;
import de.monticore.types3.SymTypeRelations;
import de.se_rwth.commons.logging.Log;

public class VariableDeclarationTypesFitCoCo implements SD4ComponentsASTSDVariableDeclarationCoCo {

  public static final String MESSAGE_ERROR = "0xB5005: "
    + "Cannot assign %s to %s";

  protected final AbstractDerive deriver;

  public VariableDeclarationTypesFitCoCo() {
    this(new FullSD4ComponentsDeriver());
  }

  public VariableDeclarationTypesFitCoCo(AbstractDerive deriver) {
    this.deriver = deriver;
  }

  @Override
  public void check(ASTSDVariableDeclaration node) {
    TypeCheckResult result = deriver.deriveType(node.getAssignment());
    if (result.isPresentResult() && !SymTypeRelations.isCompatible(node.getSymbol().getType(), result.getResult())) {
      Log.error(String.format(MESSAGE_ERROR, result.getResult().print(), node.getSymbol().getType().print()), node.get_SourcePositionStart(), node.get_SourcePositionEnd());
    }
  }
}
