/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sdbasis.types;

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.lang.sdbasis._visitor.SDBasisDelegatorVisitor;
import de.monticore.literals.mccommonliterals._ast.ASTSignedLiteral;
import de.monticore.literals.mcliteralsbasis._ast.ASTLiteral;
import de.monticore.types.check.*;
import de.monticore.types.mcbasictypes._ast.ASTMCType;

import java.util.Optional;

/**
 * Visitor for typechecking types and expressions used in SDs.
 */
public class DeriveSymTypeOfSDBasis extends SDBasisDelegatorVisitor implements ITypesCalculator {

  private TypeCheckResult typeCheckResult;

  public DeriveSymTypeOfSDBasis() {
    setRealThis(this);
    this.typeCheckResult = new TypeCheckResult();
    init();
  }

  @Override
  public void init() {
    // initializes visitors used for typechecking
    final DeriveSymTypeOfLiterals deriveSymTypeOfLiterals = new DeriveSymTypeOfLiterals();
    deriveSymTypeOfLiterals.setTypeCheckResult(getTypeCheckResult());
    setMCLiteralsBasisVisitor(deriveSymTypeOfLiterals);

    final DeriveSymTypeOfMCCommonLiterals deriveSymTypeOfMCCommonLiterals = new DeriveSymTypeOfMCCommonLiterals();
    deriveSymTypeOfMCCommonLiterals.setTypeCheckResult(getTypeCheckResult());
    setMCCommonLiteralsVisitor(deriveSymTypeOfMCCommonLiterals);

    final DeriveSymTypeOfExpression deriveSymTypeOfExpression = new DeriveSymTypeOfExpression();
    deriveSymTypeOfExpression.setTypeCheckResult(getTypeCheckResult());
    setExpressionsBasisVisitor(deriveSymTypeOfExpression);

    final SynthesizeSymTypeFromMCBasicTypes synthesizeSymTypeFromMCBasicTypes = new SynthesizeSymTypeFromMCBasicTypes();
    synthesizeSymTypeFromMCBasicTypes.setTypeCheckResult(getTypeCheckResult());
    setMCBasicTypesVisitor(synthesizeSymTypeFromMCBasicTypes);
  }

  public TypeCheckResult getTypeCheckResult() {
    return typeCheckResult;
  }

  public void setTypeCheckResult(TypeCheckResult typeCheckResult) {
    this.typeCheckResult = typeCheckResult;
  }

  public Optional<SymTypeExpression> calculateType(ASTMCType type) {
    type.accept(getRealThis());
    return Optional.of(getTypeCheckResult().getCurrentResult());
  }

  @Override
  public Optional<SymTypeExpression> calculateType(ASTExpression ex) {
    ex.accept(getRealThis());
    return Optional.of(getTypeCheckResult().getCurrentResult());
  }

  @Override
  public Optional<SymTypeExpression> calculateType(ASTLiteral lit) {
    lit.accept(getRealThis());
    return Optional.of(getTypeCheckResult().getCurrentResult());
  }

  @Override
  public Optional<SymTypeExpression> calculateType(ASTSignedLiteral lit) {
    lit.accept(getRealThis());
    return Optional.of(getTypeCheckResult().getCurrentResult());
  }
}
