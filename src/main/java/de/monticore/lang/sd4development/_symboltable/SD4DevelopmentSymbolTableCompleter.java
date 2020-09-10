/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development._symboltable;

import com.google.common.collect.Iterables;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentVisitor;
import de.monticore.lang.sdbasis._visitor.SDBasisDelegatorVisitor;
import de.monticore.lang.sdbasis._visitor.SDBasisVisitor;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.types.check.SymTypeExpressionFactory;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static de.monticore.lang.util.FQNameCalculator.calcFQNameCandidates;

public class SD4DevelopmentSymbolTableCompleter implements SD4DevelopmentVisitor {

  private static final String USED_BUT_UNDEFINED = "0xB0028: Type '%s' is used but not defined.";
  private static final String DEFINED_MUTLIPLE_TIMES = "0xB0031: Type '%s' is defined more than once.";

  private List<ASTMCImportStatement> imports = new ArrayList<>();
  private ASTMCQualifiedName packageDeclaration;

  public void complete(List<ASTMCImportStatement> imports, ASTMCQualifiedName packageDeclaration) {
    this.imports = imports;
    this.packageDeclaration = packageDeclaration;
  }

  @Override
  public void visit(VariableSymbol var) {
    String typeName = var.getType().getTypeInfo().getName();
    Set<TypeSymbol> typeSymbols = new HashSet<>();
    for (String fqNameCandidate : calcFQNameCandidates(imports, packageDeclaration, typeName)) {
      typeSymbols.addAll(var.getEnclosingScope().resolveTypeMany(fqNameCandidate));
    }

    if (typeSymbols.isEmpty()) {
      Log.error(String.format(USED_BUT_UNDEFINED, typeName), var.getAstNode().get_SourcePositionStart());
      return;
    }
    else if (typeSymbols.size() > 1) {
      Log.error(String.format(DEFINED_MUTLIPLE_TIMES, typeName), var.getAstNode().get_SourcePositionStart());
    }
    else {
      var.setType(SymTypeExpressionFactory.createTypeExpression(Iterables.getFirst(typeSymbols, null)));
    }
  }

}
