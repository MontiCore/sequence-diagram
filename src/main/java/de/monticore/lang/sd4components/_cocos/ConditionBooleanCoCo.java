/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._cocos;

import de.monticore.lang.sd4components._ast.ASTSDCondition;
import de.monticore.lang.sd4components.types.FullSD4ComponentsDeriver;
import de.monticore.types.check.AbstractDerive;
import de.monticore.types.check.TypeCheckResult;
import de.monticore.types3.SymTypeRelations;
import de.se_rwth.commons.logging.Log;

public class ConditionBooleanCoCo implements SD4ComponentsASTSDConditionCoCo {

  protected final AbstractDerive deriver;

  public ConditionBooleanCoCo() {
    this(new FullSD4ComponentsDeriver());
  }

  public ConditionBooleanCoCo(AbstractDerive deriver) {
    this.deriver = deriver;
  }

  @Override
  public void check(ASTSDCondition node) {
    TypeCheckResult result = deriver.deriveType(node.getExpression());
    if (result.isPresentResult() && !SymTypeRelations.isBoolean(result.getResult())) {
      Log.error("0xB5004: Assert expression is not boolean", node.get_SourcePositionStart(), node.get_SourcePositionEnd());
    }
  }
}
