/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development._symboltable;

import com.google.common.collect.Iterables;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentHandler;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentTraverser;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.symbols.basicsymbols._visitor.BasicSymbolsVisitor2;
import de.monticore.types.check.SymTypeExpressionFactory;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.se_rwth.commons.logging.Log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static de.monticore.lang.util.FQNameCalculator.calcFQNameCandidates;

public class SD4DevelopmentSymbolTableCompleter implements BasicSymbolsVisitor2, SD4DevelopmentHandler {

  private static final String USED_BUT_UNDEFINED = "0xB0028: Type '%s' is used but not defined.";
  private static final String DEFINED_MUTLIPLE_TIMES = "0xB0031: Type '%s' is defined more than once.";

  private final List<ASTMCImportStatement> imports;
  private final ASTMCQualifiedName packageDeclaration;
  private SD4DevelopmentTraverser traverser;

  public SD4DevelopmentSymbolTableCompleter(List<ASTMCImportStatement> imports, ASTMCQualifiedName packageDeclaration) {
    this.imports = imports;
    this.packageDeclaration = packageDeclaration;
  }

  @Override
  public SD4DevelopmentTraverser getTraverser() {
    return traverser;
  }

  @Override
  public void setTraverser(SD4DevelopmentTraverser traverser) {
    this.traverser = traverser;
  }

  @Override
  public void traverse(ISD4DevelopmentScope node) {
    SD4DevelopmentHandler.super.traverse(node);
    for (ISD4DevelopmentScope subscope : node.getSubScopes()) {
      subscope.accept(this.getTraverser());
    }
  }

  @Override
  public void visit(VariableSymbol var) {
    String typeName = var.getType().getTypeInfo().getName();
    Set<TypeSymbol> typeSymbols = new HashSet<>();
    for (String fqNameCandidate : calcFQNameCandidates(imports, packageDeclaration, typeName)) {
      SD4DevelopmentScope scope = (SD4DevelopmentScope) var.getEnclosingScope();
      typeSymbols.addAll(scope.resolveTypeMany(fqNameCandidate));
      typeSymbols.addAll(scope.resolveOOTypeMany(fqNameCandidate));
    }

    if (typeSymbols.isEmpty()) {
      Log.error(String.format(USED_BUT_UNDEFINED, typeName), var.getAstNode().get_SourcePositionStart());
    }
    else if (typeSymbols.size() > 1) {
      Log.error(String.format(DEFINED_MUTLIPLE_TIMES, typeName), var.getAstNode().get_SourcePositionStart());
    }
    else {
      TypeSymbol typeSymbol = Iterables.getFirst(typeSymbols, null);
      var.setType(SymTypeExpressionFactory.createTypeExpression(typeSymbol));
    }
  }

}
