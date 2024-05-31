/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components.types;

import de.monticore.lang.sd4components.SD4ComponentsMill;
import de.monticore.lang.sd4components._visitor.SD4ComponentsTraverser;
import de.monticore.types.check.AbstractDerive;
import de.monticore.types.check.DeriveSymTypeOfCommonExpressions;
import de.monticore.types.check.DeriveSymTypeOfExpression;
import de.monticore.types.check.DeriveSymTypeOfLiterals;
import de.monticore.types.check.DeriveSymTypeOfMCCommonLiterals;

public class FullSD4ComponentsDeriver extends AbstractDerive {

  public FullSD4ComponentsDeriver() {
    this(SD4ComponentsMill.traverser());
  }

  public FullSD4ComponentsDeriver(SD4ComponentsTraverser traverser) {
    super(traverser);
    init(traverser);
  }

  public void init(SD4ComponentsTraverser traverser) {
    // initializes visitors used for typechecking
    final DeriveSymTypeOfLiterals deriveSymTypeOfLiterals = new DeriveSymTypeOfLiterals();
    deriveSymTypeOfLiterals.setTypeCheckResult(getTypeCheckResult());
    traverser.add4MCLiteralsBasis(deriveSymTypeOfLiterals);

    final DeriveSymTypeOfMCCommonLiterals deriveSymTypeOfMCCommonLiterals = new DeriveSymTypeOfMCCommonLiterals();
    deriveSymTypeOfMCCommonLiterals.setTypeCheckResult(getTypeCheckResult());
    traverser.add4MCCommonLiterals(deriveSymTypeOfMCCommonLiterals);

    final DeriveSymTypeOfExpression deriveSymTypeOfExpression = new DeriveSymTypeOfExpression();
    deriveSymTypeOfExpression.setTypeCheckResult(getTypeCheckResult());
    traverser.setExpressionsBasisHandler(deriveSymTypeOfExpression);
    traverser.add4ExpressionsBasis(deriveSymTypeOfExpression);

    final DeriveSymTypeOfCommonExpressions deriveSymTypeOfCommonExpressions = new DeriveSymTypeOfCommonExpressions();
    deriveSymTypeOfCommonExpressions.setTypeCheckResult(getTypeCheckResult());
    traverser.setCommonExpressionsHandler(deriveSymTypeOfCommonExpressions);
    traverser.add4CommonExpressions(deriveSymTypeOfCommonExpressions);
  }
}
