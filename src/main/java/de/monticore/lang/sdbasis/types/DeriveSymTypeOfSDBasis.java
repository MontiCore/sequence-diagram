/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sdbasis.types;

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.lang.sdbasis._visitor.SDBasisDelegatorVisitor;
import de.monticore.literals.mccommonliterals._ast.ASTSignedLiteral;
import de.monticore.literals.mcliteralsbasis._ast.ASTLiteral;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.check.*;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;

/**
 * Visitor for typechecking types and expressions used in SDs.
 */
public class DeriveSymTypeOfSDBasis extends SDBasisDelegatorVisitor implements ITypesCalculator {

  private TypeCheckResult typeCheckResult;
  private final static String TYPE_COULD_NOT_BE_CALCULATED =  "0xB0035: Type of ASTMCType '%s' could not be calculated. Is the type defined?";
  private final static String EXPRESSION_COULD_NOT_BE_CALCULATED =  "0xB0036: Type of expression could not be calculated. Is the type defined?";
  private final static String LITERAL_COULD_NOT_BE_CALCULATED =  "Type of ASTLiteral could not be calculated. Is the literal defined?";
  private final static String SIGNED_LITERAL_COULD_NOT_BE_CALCULATED =  "Type of ASTSignedLiteral could not be calculated. Is the literal defined?";

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
    if (getTypeCheckResult().isPresentCurrentResult()) {
      return Optional.of(getTypeCheckResult().getCurrentResult());
    }
    else {
      Log.error(String.format(
        TYPE_COULD_NOT_BE_CALCULATED,
        type.printType(new MCBasicTypesPrettyPrinter(new IndentPrinter()))),
        type.get_SourcePositionStart(), type.get_SourcePositionEnd());
      return Optional.empty();
    }
  }

  @Override
  public Optional<SymTypeExpression> calculateType(ASTExpression ex) {
    ex.accept(getRealThis());
    if (getTypeCheckResult().isPresentCurrentResult()) {
      return Optional.of(getTypeCheckResult().getCurrentResult());
    }
    else {
      Log.error(EXPRESSION_COULD_NOT_BE_CALCULATED,
        ex.get_SourcePositionStart(),
        ex.get_SourcePositionEnd());
      return Optional.empty();
    }
  }

  @Override
  public Optional<SymTypeExpression> calculateType(ASTLiteral lit) {
    lit.accept(getRealThis());
    if (getTypeCheckResult().isPresentCurrentResult()) {
      return Optional.of(getTypeCheckResult().getCurrentResult());
    }
    else {
      Log.error(LITERAL_COULD_NOT_BE_CALCULATED,
        lit.get_SourcePositionStart(),
        lit.get_SourcePositionEnd());
      return Optional.empty();
    }
  }

  @Override
  public Optional<SymTypeExpression> calculateType(ASTSignedLiteral lit) {
    lit.accept(getRealThis());
    if (getTypeCheckResult().isPresentCurrentResult()) {
      return Optional.of(getTypeCheckResult().getCurrentResult());
    }
    else {
      Log.error(SIGNED_LITERAL_COULD_NOT_BE_CALCULATED,
        lit.get_SourcePositionStart(),
        lit.get_SourcePositionEnd());
      return Optional.empty();
    }
  }
}
