/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sdbasis._symboltable;

import de.monticore.lang.sdbasis._ast.ASTSDBody;
import de.monticore.lang.sdbasis._ast.ASTSDObject;
import de.monticore.lang.sdbasis.types.DeriveSymTypeOfSDBasis;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.symbols.oosymbols._symboltable.OOTypeSymbol;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.SymTypeExpressionFactory;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.se_rwth.commons.logging.Log;

import java.util.Deque;
import java.util.Optional;

public class SDBasisSymbolTableCreator extends SDBasisSymbolTableCreatorTOP {

  private final DeriveSymTypeOfSDBasis typeChecker = new DeriveSymTypeOfSDBasis();

  /**
   * prettyPrinter used for error reporting.
   */
  private final MCBasicTypesPrettyPrinter prettyPrinter = new MCBasicTypesPrettyPrinter(new IndentPrinter());

  public SDBasisSymbolTableCreator(ISDBasisScope enclosingScope) {
    super(enclosingScope);
  }

  public SDBasisSymbolTableCreator(Deque<? extends ISDBasisScope> scopeStack) {
    super(scopeStack);
  }

  @Override
  public void visit(ASTSDObject node) {
    VariableSymbol symbol = create_SDObject(node);
    if (getCurrentScope().isPresent()) {
      symbol.setEnclosingScope(getCurrentScope().get());
    }
    addToScopeAndLinkWithNode(symbol, node);
    initialize_SDObject(symbol, node);
  }

  @Override
  protected void initialize_SDObject(VariableSymbol symbol, ASTSDObject ast) {
    if (ast.isPresentMCObjectType()) {
      ast.getMCObjectType().setEnclosingScope(ast.getEnclosingScope());
      final Optional<SymTypeExpression> typeResult = typeChecker.calculateType(ast.getMCObjectType());
      if (!typeResult.isPresent()) {
        Log.error(String.format("0xB0001: The type (%s) of the object (%s) could not be calculated", prettyPrinter.prettyprint(ast.getMCObjectType()), ast.getName()));
      }
      else {
        symbol.setType(typeResult.get());
      }
    }
    else {
      OOTypeSymbol objectType = new OOTypeSymbol("Object");
      symbol.setType(SymTypeExpressionFactory.createTypeExpression(objectType));
    }
  }
}