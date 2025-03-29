package de.monticore.lang.sdbasis._symboltable;

import de.monticore.lang.sdbasis._ast.ASTSDObject;
import de.monticore.lang.sdbasis._visitor.SDBasisVisitor2;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.symbols.oosymbols._symboltable.OOTypeSymbol;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.SymTypeExpressionFactory;
import de.monticore.types.mcbasictypes._ast.ASTMCObjectType;
import de.monticore.types3.TypeCheck3;

public class SDBasisSymbolTableCompleter implements SDBasisVisitor2 {

  @Override
  public void endVisit(ASTSDObject node) {
    VariableSymbol symbol = node.getSymbol();

    if (node.isPresentMCObjectType()) {
      ASTMCObjectType objectType = node.getMCObjectType();

      final SymTypeExpression typeResult = TypeCheck3.symTypeFromAST(objectType);
      if (!typeResult.isObscureType()) {
        symbol.setType(typeResult);
      }
    } else {
      OOTypeSymbol objectType = new OOTypeSymbol("Object");
      symbol.setType(SymTypeExpressionFactory.createTypeExpression(objectType));
    }
  }
}
