// (c) https://github.com/MontiCore/monticore
package de.monticore.lang.sdbasis.types3;

import de.monticore.expressions.expressionsbasis.types3.ExpressionBasisCTTIVisitor;
import de.monticore.lang.sdbasis.SDBasisMill;
import de.monticore.lang.sdbasis._visitor.SDBasisTraverser;
import de.monticore.literals.mccommonliterals.types3.MCCommonLiteralsTypeVisitor;
import de.monticore.types.mcbasictypes.types3.MCBasicTypesTypeVisitor;
import de.monticore.types3.SymTypeRelations;
import de.monticore.types3.Type4Ast;
import de.monticore.types3.generics.context.InferenceContext4Ast;
import de.monticore.types3.util.MapBasedTypeCheck3;
import de.monticore.types3.util.NameExpressionTypeCalculator;
import de.monticore.types3.util.WithinScopeBasicSymbolsResolver;
import de.monticore.types3.util.WithinTypeBasicSymbolsResolver;
import de.monticore.visitor.ITraverser;
import de.se_rwth.commons.logging.Log;

/**
 * TypeCheck3 implementation for the SDBasis language.
 * After calling {@link #init()},
 * this implementation will be available through the TypeCheck3 interface.
 */
public class SDBasisTypeCheck3 extends MapBasedTypeCheck3 {

  public static void init() {
    initTC3Delegate();
    SymTypeRelations.init();
  }

  protected static void initTC3Delegate() {
    Log.trace("init SDBasisTypeCheck3", "TypeCheck setup");

    SDBasisTraverser traverser = SDBasisMill.inheritanceTraverser();
    Type4Ast type4Ast = new Type4Ast();
    InferenceContext4Ast ctx4Ast = new InferenceContext4Ast();

    // for some Visitors
    WithinTypeBasicSymbolsResolver withinTypeBasicSymbolsResolver =
      new WithinTypeBasicSymbolsResolver();
    WithinScopeBasicSymbolsResolver withinScopeBasicSymbolsResolver =
      new NameExpressionTypeCalculator();

    // Expressions

    ExpressionBasisCTTIVisitor visExpressionBasis = new ExpressionBasisCTTIVisitor();
    visExpressionBasis.setWithinScopeResolver(withinScopeBasicSymbolsResolver);
    visExpressionBasis.setType4Ast(type4Ast);
    visExpressionBasis.setContext4Ast(ctx4Ast);
    traverser.add4ExpressionsBasis(visExpressionBasis);
    traverser.setExpressionsBasisHandler(visExpressionBasis);

    MCCommonLiteralsTypeVisitor visMCCommonLiterals = new MCCommonLiteralsTypeVisitor();
    visMCCommonLiterals.setType4Ast(type4Ast);
    traverser.add4MCCommonLiterals(visMCCommonLiterals);

    // MCTypes

    MCBasicTypesTypeVisitor visMCBasicTypes = new MCBasicTypesTypeVisitor();
    visMCBasicTypes.setWithinTypeResolver(withinTypeBasicSymbolsResolver);
    visMCBasicTypes.setWithinScopeResolver(withinScopeBasicSymbolsResolver);
    visMCBasicTypes.setType4Ast(type4Ast);
    traverser.add4MCBasicTypes(visMCBasicTypes);

    // create delegate
    SDBasisTypeCheck3 oclTC3 = new SDBasisTypeCheck3(traverser, type4Ast, ctx4Ast);
    oclTC3.setThisAsDelegate();
  }

  protected SDBasisTypeCheck3(
    ITraverser typeTraverser, Type4Ast type4Ast, InferenceContext4Ast ctx4Ast) {
    super(typeTraverser, type4Ast, ctx4Ast);
  }
}
