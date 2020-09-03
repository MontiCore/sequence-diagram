package de.monticore.lang;

import de.monticore.lang.sd4development._symboltable.ISD4DevelopmentGlobalScope;
import de.monticore.lang.sd4development._symboltable.SD4DevelopmentScope;
import de.monticore.lang.sdbasis.types.DeriveSymTypeOfSDBasis;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.symbols.oosymbols._symboltable.*;
import de.monticore.types.MCTypeFacade;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class TestUtils {

  private static final MCTypeFacade typeFacade = MCTypeFacade.getInstance();

  static void setupGlobalScope(ISD4DevelopmentGlobalScope globalScope) {
    addPrimitiveTypeSymbols(globalScope);
    addOOTypeSymbols(globalScope);
    addVariableSymbols(globalScope);
  }

  private static void addPrimitiveTypeSymbols(ISD4DevelopmentGlobalScope globalScope) {
    Stream.of(
      new TypeSymbol("int")
    ).forEach(t -> {
      t.setEnclosingScope(globalScope);
      globalScope.add(t);
    });
  }

  private static void addOOTypeSymbols(ISD4DevelopmentGlobalScope globalScope) {
    List<OOTypeSymbol> ooTypes = Stream.of(
      "BidMessage", "Auction", "NotASubType",
      "Protocol", "Factory", "AuctionTest", "Person", "Order", "Customer", "Mail",
      "A", "B", "C", "D", "E", "F", "G", "H", "ExceptionTyp")
                                           .map(OOTypeSymbol::new)
                                           .collect(Collectors.toList());
    ooTypes.add(createBiddingPolicyOOTypeSymbol());
    ooTypes.add(createTimingPolicyOOTypeSymbol());
    ooTypes.forEach(t -> {
      t.setEnclosingScope(globalScope);
      globalScope.add(t);
      globalScope.add((TypeSymbol) t);
    });
  }

  private static OOTypeSymbol createBiddingPolicyOOTypeSymbol() {
    // validateBid method symbol
    MethodSymbol validateBid = new MethodSymbolBuilder()
      .setName("validateBid")
      .setReturnType(new DeriveSymTypeOfSDBasis().calculateType(typeFacade.createIntType()).get())
      .build();
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
    MethodSymbol newCurrentClosingTime = new MethodSymbolBuilder()
      .setReturnType(new DeriveSymTypeOfSDBasis().calculateType(typeFacade.createIntType()).get())
      .setName("newCurrentClosingTime")
      .build();
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
    kupfer912.setType(new DeriveSymTypeOfSDBasis().calculateType(typeFacade.createQualifiedType("Auction")).get());
    return kupfer912;
  }
}
