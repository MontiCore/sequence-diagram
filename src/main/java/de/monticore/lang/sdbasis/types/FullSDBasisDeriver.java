/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sdbasis.types;

import de.monticore.lang.sdbasis.SDBasisMill;
import de.monticore.lang.sdbasis._visitor.SDBasisTraverser;
import de.monticore.types.check.AbstractDerive;
import de.monticore.types.check.DeriveSymTypeOfExpression;
import de.monticore.types.check.DeriveSymTypeOfLiterals;
import de.monticore.types.check.DeriveSymTypeOfMCCommonLiterals;
import de.monticore.visitor.ITraverser;

public class FullSDBasisDeriver extends AbstractDerive {

  public FullSDBasisDeriver(){
    this(SDBasisMill.traverser());
  }

  public FullSDBasisDeriver(SDBasisTraverser traverser) {
    super(traverser);
    init(traverser);
  }

  public void init(SDBasisTraverser traverser){
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
  }
}
