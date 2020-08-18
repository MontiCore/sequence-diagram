package de.monticore.lang.sd4development._cocos;

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.lang.sd4development._ast.ASTSDCall;
import de.monticore.lang.sd4development.prettyprint.SD4DevelopmentDelegatorPrettyPrinter;
import de.monticore.lang.sdbasis._ast.ASTSDObjectTarget;
import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSDSendMessageCoCo;
import de.monticore.lang.sdbasis.types.DeriveSymTypeOfSDBasis;
import de.monticore.symbols.oosymbols._symboltable.MethodSymbol;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.TypeCheck;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;

/**
 * Checks if a method action is valid, i.e., whether a method with a
 * corresponding signature is defined for the type of the target of
 * the interaction. The name of the method as well as the number
 * and types of method parameters must be equal.
 */
public class MethodActionValidCoco implements SDBasisASTSDSendMessageCoCo {

  private static final String MESSAGE = "0xB0012: " +
    "'%s' type does not contain method '%s'.";

  private final DeriveSymTypeOfSDBasis deriveSymTypeOfSDBasis;

  private final SD4DevelopmentDelegatorPrettyPrinter prettyPrinter;

  public MethodActionValidCoco() {
    this.deriveSymTypeOfSDBasis = new DeriveSymTypeOfSDBasis();
    this.prettyPrinter = new SD4DevelopmentDelegatorPrettyPrinter();
  }

  @Override
  public void check(ASTSDSendMessage node) {
    if (isSDCallAndObjectTargetPresent(node)) {
      ASTSDCall call = (ASTSDCall) node.getSDAction();
      SymTypeExpression targetType = ((ASTSDObjectTarget) node.getSDTarget()).getNameSymbol().getType();

      for (MethodSymbol methodSymbol : targetType.getMethodList(call.getName())) {
        if (methodAndCallMatch(methodSymbol, call)) {
          return;
        }
      }
      Log.warn(String.format(MESSAGE, targetType.getTypeInfo().getName(), call.getName()));
    }
  }

  private boolean isSDCallAndObjectTargetPresent(ASTSDSendMessage node) {
    return node.getSDAction() instanceof ASTSDCall && node.isPresentSDTarget() && node.getSDTarget() instanceof ASTSDObjectTarget;
  }

  /**
   * This method checks, if a method invocation, i.e., the call, matches with a given MethodSymbol, i.e., the declaration
   *
   * @param methodSymbol the method declaration
   * @param call the invocation of a method
   * @return true, if the signature of call corresponds with the signature of the given method symbol
   */
  private boolean methodAndCallMatch(MethodSymbol methodSymbol, ASTSDCall call) {
    if (!isSameParameterSize(methodSymbol, call)) {
      return false;
    }
    for (int i = 0; i < methodSymbol.getParameterList().size(); i++) {
      SymTypeExpression methodParameterType = methodSymbol.getParameterList().get(i).getType();
      ASTExpression callArgument = call.getArguments().getExpressions(i);
      Optional<SymTypeExpression> callArgumentType = deriveSymTypeOfSDBasis.calculateType(callArgument);

      if (!callArgumentType.isPresent()) {
        Log.info(String.format("Type of method argument '%s' could not be determined", prettyPrinter.prettyPrint(callArgument)),
          this.getClass().getCanonicalName());
        return false;
      }

      if (!TypeCheck.compatible(methodParameterType, callArgumentType.get())) {
        return false;
      }
    }
    return true;
  }

  private boolean isSameParameterSize(MethodSymbol methodSymbol, ASTSDCall call) {
    return methodSymbol.getParameterList().size() == call.getArguments().getExpressionsList().size();
  }
}
