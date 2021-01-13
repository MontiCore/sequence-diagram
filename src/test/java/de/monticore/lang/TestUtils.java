/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang;

import com.google.common.collect.Lists;
import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sd4development._symboltable.ISD4DevelopmentGlobalScope;
import de.monticore.lang.sd4development._symboltable.SD4DevelopmentArtifactScope;
import de.monticore.lang.sd4development._symboltable.SD4DevelopmentScope;
import de.monticore.lang.sdbasis.types.DeriveSymTypeOfSDBasis;
import de.monticore.symbols.basicsymbols._symboltable.DiagramSymbol;
import de.monticore.symbols.basicsymbols._symboltable.DiagramSymbolBuilder;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.symbols.oosymbols._symboltable.*;
import de.monticore.types.MCTypeFacade;
import de.monticore.types.check.SymTypeExpressionFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TestUtils {

  private static final MCTypeFacade typeFacade = MCTypeFacade.getInstance();

  public static void setupGlobalScope(ISD4DevelopmentGlobalScope globalScope) {
    addPrimitiveTypeSymbols(globalScope);
    addOOTypeSymbols(globalScope);
    addVariableSymbols(globalScope);
  }

  private static void addPrimitiveTypeSymbols(ISD4DevelopmentGlobalScope globalScope) {
    Stream.of(new TypeSymbol("int")).forEach(t -> {
      t.setEnclosingScope(globalScope);
      globalScope.add(t);
    });
  }

  private static void addOOTypeSymbols(ISD4DevelopmentGlobalScope globalScope) {
    List<OOTypeSymbol> ooTypes = Stream.of("BidMessage", "Auction", "NotASubType", "Protocol", "Factory", "AuctionTest", "Person", "Order", "Customer", "Mail", "A", "B", "C", "D", "E", "F", "G", "H", "ExceptionTyp").map(OOTypeSymbol::new).collect(Collectors.toList());
    ooTypes.add(createBiddingPolicyOOTypeSymbol());
    ooTypes.add(createTimingPolicyOOTypeSymbol());
    ooTypes.forEach(t -> {
      t.setEnclosingScope(globalScope);
      globalScope.add(t);
      globalScope.add((TypeSymbol) t);
    });

    SD4DevelopmentArtifactScope scopeContainingDeepOOTypeSymbol = new SD4DevelopmentArtifactScope(Optional.of(globalScope), "very.very.deep", Lists.newArrayList());
    OOTypeSymbol deepOOTypeSymbol = createDeepOOTypeSymbol();
    deepOOTypeSymbol.setEnclosingScope(scopeContainingDeepOOTypeSymbol);
    scopeContainingDeepOOTypeSymbol.add(deepOOTypeSymbol);
    scopeContainingDeepOOTypeSymbol.add((TypeSymbol) deepOOTypeSymbol);
    globalScope.addSubScope(scopeContainingDeepOOTypeSymbol);
    scopeContainingDeepOOTypeSymbol.setEnclosingScope(globalScope);
    DiagramSymbol d = new DiagramSymbolBuilder().setName("DeepType").build();
    scopeContainingDeepOOTypeSymbol.add(d);
  }

  private static OOTypeSymbol createDeepOOTypeSymbol() {
    MethodSymbol someMethod = new MethodSymbolBuilder().setReturnType(new DeriveSymTypeOfSDBasis(SD4DevelopmentMill.traverser()).calculateType(typeFacade.createIntType()).get()).setName("someMethod").build();
    SD4DevelopmentScope scope = new SD4DevelopmentScope();
    FieldSymbol valueField = new FieldSymbolBuilder().setName("value").setType(new DeriveSymTypeOfSDBasis().calculateType(typeFacade.createIntType()).get()).build();
    scope.add(valueField);
    scope.add((VariableSymbol) valueField);
    someMethod.setSpannedScope(scope);
    // TimingPolicy oo type symbol
    OOTypeSymbol deepType = new OOTypeSymbol("DeepType");
    deepType.setSpannedScope(new SD4DevelopmentScope());
    deepType.addMethodSymbol(someMethod);
    deepType.addFunctionSymbol(someMethod);
    return deepType;
  }

  private static OOTypeSymbol createBiddingPolicyOOTypeSymbol() {
    // validateBid method symbol
    MethodSymbol validateBid = new MethodSymbolBuilder().setName("validateBid").setReturnType(new DeriveSymTypeOfSDBasis().calculateType(typeFacade.createIntType()).get()).build();
    SD4DevelopmentScope scope = new SD4DevelopmentScope();
    FieldSymbol valueField = new FieldSymbolBuilder().setName("value").setType(new DeriveSymTypeOfSDBasis().calculateType(typeFacade.createIntType()).get()).build();
    scope.add(valueField);
    scope.add((VariableSymbol) valueField);
    validateBid.setSpannedScope(scope);
    // BiddingPolicy oo type symbol
    OOTypeSymbol biddingPolicy = new OOTypeSymbol("BiddingPolicy");
    biddingPolicy.setSpannedScope(new SD4DevelopmentScope());
    biddingPolicy.addMethodSymbol(validateBid);
    biddingPolicy.addFunctionSymbol(validateBid);
    return biddingPolicy;
  }

  private static OOTypeSymbol createTimingPolicyOOTypeSymbol() {
    // newCurrentClosingTime method symbol
    MethodSymbol newCurrentClosingTime = new MethodSymbolBuilder().setReturnType(new DeriveSymTypeOfSDBasis().calculateType(typeFacade.createIntType()).get()).setName("newCurrentClosingTime").build();
    SD4DevelopmentScope scope = new SD4DevelopmentScope();
    FieldSymbol valueField = new FieldSymbolBuilder().setName("value").setType(new DeriveSymTypeOfSDBasis().calculateType(typeFacade.createIntType()).get()).build();
    scope.add(valueField);
    scope.add((VariableSymbol) valueField);
    newCurrentClosingTime.setSpannedScope(scope);
    // TimingPolicy oo type symbol
    OOTypeSymbol timingPolicy = new OOTypeSymbol("TimingPolicy");
    timingPolicy.setSpannedScope(new SD4DevelopmentScope());
    timingPolicy.addMethodSymbol(newCurrentClosingTime);
    timingPolicy.addFunctionSymbol(newCurrentClosingTime);
    return timingPolicy;
  }

  private static void addVariableSymbols(ISD4DevelopmentGlobalScope globalScope) {
    VariableSymbol kupfer912 = createKupfer912VariableSymbol();
    kupfer912.setEnclosingScope(globalScope);
    globalScope.add(kupfer912);
  }

  private static VariableSymbol createKupfer912VariableSymbol() {
    VariableSymbol kupfer912 = new VariableSymbol("kupfer912");
    kupfer912.setPackageName("foo.bar.auctiontypes");
    OOTypeSymbol auctionType = new OOTypeSymbol("Auction");
    kupfer912.setType(SymTypeExpressionFactory.createTypeExpression(auctionType));
    return kupfer912;
  }
}
