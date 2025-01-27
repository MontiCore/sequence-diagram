/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._cocos;

import de.monticore.lang.sd4components._ast.ASTSDVariableDeclaration;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types3.SymTypeRelations;
import de.monticore.types3.TypeCheck3;
import de.se_rwth.commons.logging.Log;

public class VariableDeclarationTypesFitCoCo implements SD4ComponentsASTSDVariableDeclarationCoCo {

  public static final String MESSAGE_ERROR = "0xB5005: "
    + "Cannot assign %s to %s";

  @Override
  public void check(ASTSDVariableDeclaration node) {
    SymTypeExpression target = node.getSymbol().getType();
    SymTypeExpression result = TypeCheck3.typeOf(node.getAssignment(), target);
    if (!result.isObscureType() && !SymTypeRelations.isCompatible(target, result)) {
      Log.error(String.format(MESSAGE_ERROR, result.printFullName(), node.getSymbol().getType().printFullName()), node.get_SourcePositionStart(), node.get_SourcePositionEnd());
    }
  }
}
