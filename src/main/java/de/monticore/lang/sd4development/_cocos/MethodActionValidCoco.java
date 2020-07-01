package de.monticore.lang.sd4development._cocos;

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.lang.sd4development._ast.ASTSDCall;
import de.monticore.lang.sdbasis._ast.ASTSDObjectTarget;
import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSDSendMessageCoCo;
import de.monticore.lang.sdbasis.types.DeriveSymTypeOfSDBasis;
import de.monticore.types.basictypesymbols._symboltable.VariableSymbol;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.TypeCheck;
import de.monticore.types.typesymbols._symboltable.FieldSymbol;
import de.monticore.types.typesymbols._symboltable.MethodSymbol;
import de.se_rwth.commons.logging.Log;

import java.util.List;
import java.util.Optional;

public class MethodActionValidCoco implements SDBasisASTSDSendMessageCoCo {

  private final DeriveSymTypeOfSDBasis deriveSymTypeOfSDBasis;

  public MethodActionValidCoco() {
    this.deriveSymTypeOfSDBasis = new DeriveSymTypeOfSDBasis();
  }

  @Override
  public void check(ASTSDSendMessage node) {
    if (node.getSDAction() instanceof ASTSDCall) {
      ASTSDCall call = (ASTSDCall) node.getSDAction();

      SymTypeExpression targetType = ((ASTSDObjectTarget) node.getSDTarget()).getNameSymbol().getType();


      outer:
      for (MethodSymbol m : targetType.getMethodList(call.getName())) {

        if (m.getParameterList().size() != call.getArguments().getExpressionList().size()) {
          continue;
        }
        for (int i = 0; i < m.getParameterList().size(); i++) {
          SymTypeExpression methodParameterType = m.getParameterList().get(i).getType();
          SymTypeExpression callArgumentType = deriveSymTypeOfSDBasis.calculateType(call.getArguments().getExpression(i)).orElseThrow(IllegalStateException::new);

          if (!TypeCheck.compatible(methodParameterType, callArgumentType)) {
            continue outer;
          }
        }
        return;
      }
      Log.error("FAIL!!!");
    }
  }
}
