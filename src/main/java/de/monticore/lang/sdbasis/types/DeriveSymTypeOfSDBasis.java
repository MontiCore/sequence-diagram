/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sdbasis.types;

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sdbasis._visitor.SDBasisHandler;
import de.monticore.lang.sdbasis._visitor.SDBasisTraverser;
import de.monticore.lang.sdbasis._visitor.SDBasisVisitor2;
import de.monticore.literals.mccommonliterals._ast.ASTSignedLiteral;
import de.monticore.literals.mcliteralsbasis._ast.ASTLiteral;
import de.monticore.types.check.*;
import de.monticore.types.mcbasictypes._ast.ASTMCType;

import java.util.Optional;

/**
 * Visitor for typechecking types and expressions used in SDs.
 */
public class DeriveSymTypeOfSDBasis implements SDBasisVisitor2, SDBasisHandler {

  private TypeCheckResult typeCheckResult;
  private SDBasisTraverser traverser;

  public DeriveSymTypeOfSDBasis() {
    this.typeCheckResult = new TypeCheckResult();
    this.traverser = SD4DevelopmentMill.traverser();
    init();
  }

  public DeriveSymTypeOfSDBasis(SDBasisTraverser traverser) {
    this.typeCheckResult = new TypeCheckResult();
    this.traverser = traverser;
    init();
  }

  @Override
  public SDBasisTraverser getTraverser() {
    return traverser;
  }

  @Override
  public void setTraverser(SDBasisTraverser traverser) {
    this.traverser = traverser;
  }

  public void init() {
    traverser.setSDBasisHandler(this);
    traverser.add4SDBasis(this);

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

    final SynthesizeSymTypeFromMCBasicTypes synthesizeSymTypeFromMCBasicTypes = new SynthesizeSymTypeFromMCBasicTypes();
    synthesizeSymTypeFromMCBasicTypes.setTypeCheckResult(getTypeCheckResult());
    traverser.setMCBasicTypesHandler(synthesizeSymTypeFromMCBasicTypes);
    traverser.add4MCBasicTypes(synthesizeSymTypeFromMCBasicTypes);
  }

  public TypeCheckResult getTypeCheckResult() {
    return typeCheckResult;
  }

  public Optional<SymTypeExpression> calculateType(ASTMCType type) {
    type.accept(getTraverser());
    if (getTypeCheckResult().isPresentCurrentResult()) {
      return Optional.of(getTypeCheckResult().getCurrentResult());
    }
    else {
      return Optional.empty();
    }
  }

  public Optional<SymTypeExpression> calculateType(ASTExpression ex) {
    ex.accept(getTraverser());
    if (getTypeCheckResult().isPresentCurrentResult()) {
      return Optional.of(getTypeCheckResult().getCurrentResult());
    }
    else {
      return Optional.empty();
    }
  }

  public Optional<SymTypeExpression> calculateType(ASTLiteral lit) {
    lit.accept(getTraverser());
    if (getTypeCheckResult().isPresentCurrentResult()) {
      return Optional.of(getTypeCheckResult().getCurrentResult());
    }
    else {
      return Optional.empty();
    }
  }

  public Optional<SymTypeExpression> calculateType(ASTSignedLiteral lit) {
    lit.accept(getTraverser());
    if (getTypeCheckResult().isPresentCurrentResult()) {
      return Optional.of(getTypeCheckResult().getCurrentResult());
    }
    else {
      return Optional.empty();
    }
  }
}
