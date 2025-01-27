/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._cocos;

import de.monticore.lang.sd4components._ast.ASTSDCondition;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types3.SymTypeRelations;
import de.monticore.types3.TypeCheck3;
import de.se_rwth.commons.logging.Log;

/**
 * WARN: as this CoCo checks OCL conditions,
 * the TypeCheck has to be initialized accordingly.
 */
public class ConditionBooleanCoCo implements SD4ComponentsASTSDConditionCoCo {

  public static final String MESSAGE_ERROR = "0xB5004: "
    + "Assert expression is not boolean";

  @Override
  public void check(ASTSDCondition node) {
    SymTypeExpression result = TypeCheck3.typeOf(node.getExpression());
    if (!result.isObscureType() && !SymTypeRelations.isBoolean(result)) {
      Log.error(MESSAGE_ERROR, node.get_SourcePositionStart(), node.get_SourcePositionEnd());
    }
  }
}
