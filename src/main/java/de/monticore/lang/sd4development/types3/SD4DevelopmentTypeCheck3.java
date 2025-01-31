// (c) https://github.com/MontiCore/monticore
package de.monticore.lang.sd4development.types3;

import de.monticore.expressions.commonexpressions.types3.CommonExpressionsCTTIVisitor;
import de.monticore.expressions.expressionsbasis.types3.ExpressionBasisCTTIVisitor;
import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentTraverser;
import de.monticore.literals.mccommonliterals.types3.MCCommonLiteralsTypeVisitor;
import de.monticore.ocl.oclexpressions.types3.OCLExpressionsTypeVisitor;
import de.monticore.types.mcbasictypes.types3.MCBasicTypesTypeVisitor;
import de.monticore.types3.SymTypeRelations;
import de.monticore.types3.Type4Ast;
import de.monticore.types3.generics.context.InferenceContext4Ast;
import de.monticore.types3.util.MapBasedTypeCheck3;
import de.monticore.types3.util.OOWithinScopeBasicSymbolsResolver;
import de.monticore.types3.util.OOWithinTypeBasicSymbolsResolver;
import de.monticore.types3.util.TypeContextCalculator;
import de.monticore.types3.util.TypeVisitorLifting;
import de.monticore.types3.util.TypeVisitorOperatorCalculator;
import de.monticore.visitor.ITraverser;
import de.se_rwth.commons.logging.Log;

/**
 * TypeCheck3 implementation for the SD4Development language.
 * After calling {@link #init()},
 * this implementation will be available through the TypeCheck3 interface.
 */
public class SD4DevelopmentTypeCheck3 extends MapBasedTypeCheck3 {

  public static void init() {
    initTC3Delegate();
    SymTypeRelations.init();
    OOWithinTypeBasicSymbolsResolver.init();
    OOWithinScopeBasicSymbolsResolver.init();
    TypeContextCalculator.init();
    TypeVisitorOperatorCalculator.init();
    TypeVisitorLifting.init();
  }

  protected static void initTC3Delegate() {
    Log.trace("init SD4DevelopmentTypeCheck3", "TypeCheck setup");

    SD4DevelopmentTraverser traverser = SD4DevelopmentMill.inheritanceTraverser();
    Type4Ast type4Ast = new Type4Ast();
    InferenceContext4Ast ctx4Ast = new InferenceContext4Ast();

    // Expressions

    CommonExpressionsCTTIVisitor visCommonExpressions = new CommonExpressionsCTTIVisitor();
    visCommonExpressions.setType4Ast(type4Ast);
    visCommonExpressions.setContext4Ast(ctx4Ast);
    traverser.add4CommonExpressions(visCommonExpressions);
    traverser.setCommonExpressionsHandler(visCommonExpressions);

    ExpressionBasisCTTIVisitor visExpressionBasis = new ExpressionBasisCTTIVisitor();
    visExpressionBasis.setType4Ast(type4Ast);
    visExpressionBasis.setContext4Ast(ctx4Ast);
    traverser.add4ExpressionsBasis(visExpressionBasis);
    traverser.setExpressionsBasisHandler(visExpressionBasis);

    MCCommonLiteralsTypeVisitor visMCCommonLiterals = new MCCommonLiteralsTypeVisitor();
    visMCCommonLiterals.setType4Ast(type4Ast);
    traverser.add4MCCommonLiterals(visMCCommonLiterals);

    OCLExpressionsTypeVisitor visOCLExpressions = new OCLExpressionsTypeVisitor();
    visOCLExpressions.setType4Ast(type4Ast);
    traverser.add4OCLExpressions(visOCLExpressions);

    // MCTypes

    MCBasicTypesTypeVisitor visMCBasicTypes = new MCBasicTypesTypeVisitor();
    visMCBasicTypes.setType4Ast(type4Ast);
    traverser.add4MCBasicTypes(visMCBasicTypes);

    // create delegate
    SD4DevelopmentTypeCheck3 oclTC3 = new SD4DevelopmentTypeCheck3(traverser, type4Ast, ctx4Ast);
    oclTC3.setThisAsDelegate();
  }

  protected SD4DevelopmentTypeCheck3(
    ITraverser typeTraverser, Type4Ast type4Ast, InferenceContext4Ast ctx4Ast) {
    super(typeTraverser, type4Ast, ctx4Ast);
  }
}
