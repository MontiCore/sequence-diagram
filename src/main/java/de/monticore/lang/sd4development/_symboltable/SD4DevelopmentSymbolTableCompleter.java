/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development._symboltable;

import com.google.common.collect.Iterables;
import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sd4development._ast.ASTSDNew;
import de.monticore.lang.sd4development._ast.ASTSDVariableDeclaration;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentHandler;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentTraverser;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentVisitor2;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis._ast.ASTSequenceDiagram;
import de.monticore.lang.sdbasis._symboltable.SDBasisSymbolTableCompleter;
import de.monticore.lang.sdbasis.types.FullSDBasisSynthesizer;
import de.monticore.symbols.basicsymbols._ast.ASTDiagram;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.symbols.basicsymbols._visitor.BasicSymbolsVisitor2;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.SymTypeExpressionFactory;
import de.monticore.types.check.TypeCheckResult;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static de.monticore.lang.util.FQNameCalculator.calcFQNameCandidates;

public class SD4DevelopmentSymbolTableCompleter extends SDBasisSymbolTableCompleter implements BasicSymbolsVisitor2, SD4DevelopmentHandler, SD4DevelopmentVisitor2 {

  private static final String USED_BUT_UNDEFINED = "0xB0038: Type '%s' is used but not defined.";
  private static final String DEFINED_MUTLIPLE_TIMES = "0xB0100: Type '%s' is defined more than once.";

  private final List<ASTMCImportStatement> imports;
  private final ASTMCQualifiedName packageDeclaration;
  private SD4DevelopmentTraverser traverser;
  // TODO: remove with TypeCheck3!
  private final FullSDBasisSynthesizer synthesizer = new FullSDBasisSynthesizer();

  private List<VariableSymbol> variableSymbols = new ArrayList<>();

  public SD4DevelopmentSymbolTableCompleter(List<ASTMCImportStatement> imports, ASTMCQualifiedName packageDeclaration) {
    this.imports = imports;
    this.packageDeclaration = packageDeclaration;
  }

  public static void apply(ASTSDArtifact ast){
    ASTMCQualifiedName packageDeclaration = ast.isPresentPackageDeclaration() ? ast.getPackageDeclaration() : SD4DevelopmentMill.mCQualifiedNameBuilder().build();

    SD4DevelopmentSymbolTableCompleter stCompleter = new SD4DevelopmentSymbolTableCompleter(ast.getMCImportStatementList(), packageDeclaration);
    SD4DevelopmentTraverser t = SD4DevelopmentMill.traverser();

    t.add4BasicSymbols(stCompleter);
    t.setSD4DevelopmentHandler(stCompleter);
    t.add4SD4Development(stCompleter);
    t.add4SDBasis(stCompleter);
    stCompleter.setTraverser(t);

    ast.accept(t);
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
  public void endVisit(ASTSequenceDiagram node) {
    System.out.println("Fixing variable symbols " + variableSymbols);
    for(var var : variableSymbols) {
      SymTypeExpression type = var.getType();
      if (!type.isObscureType()) {
        String typeName = type.getTypeInfo().getName();
        Set<TypeSymbol> typeSymbols = new HashSet<>();
        for (String fqNameCandidate : calcFQNameCandidates(imports, packageDeclaration, typeName)) {
          SD4DevelopmentScope scope = (SD4DevelopmentScope) var.getEnclosingScope();
          typeSymbols.addAll(scope.resolveTypeMany(fqNameCandidate));
          typeSymbols.addAll(scope.resolveOOTypeMany(fqNameCandidate));
        }

        if (typeSymbols.isEmpty()) {
          Log.error(String.format(USED_BUT_UNDEFINED, typeName), var.getAstNode().get_SourcePositionStart());
        } else if (typeSymbols.size() > 1) {
          Log.error(String.format(DEFINED_MUTLIPLE_TIMES, typeName), var.getAstNode().get_SourcePositionStart());
        } else {
          TypeSymbol typeSymbol = Iterables.getFirst(typeSymbols, null);
          var.setType(SymTypeExpressionFactory.createTypeExpression(typeSymbol));
        }
      }
    }
  }

  @Override
  public void visit(VariableSymbol var) {
    variableSymbols.add(var);
  }

  @Override
  public void visit(ASTSDNew node)  {
    VariableSymbol symbol = node.getSymbol();

    final TypeCheckResult typeResult = synthesizer.synthesizeType(node.getDeclarationType());
    if (!typeResult.isPresentResult()) {
      Log.error(String.format("0xB0002: The type (%s) of the object (%s) could not be calculated",
        SD4DevelopmentMill.prettyPrint(node.getDeclarationType(), false),
        node.getName()));
    } else {
      symbol.setType(typeResult.getResult());
    }
  }


  @Override
  public void visit(ASTSDVariableDeclaration node) {
    VariableSymbol symbol = node.getSymbol();

    final TypeCheckResult typeResult = synthesizer.synthesizeType(node.getMCType());
    if (!typeResult.isPresentResult()) {
      Log.error(String.format("0xB0004: The type (%s) of the variable (%s) could not be calculated",
        SD4DevelopmentMill.prettyPrint(node.getMCType(), false),
        node.getName()));
    } else {
      symbol.setType(typeResult.getResult());
    }
  }
}
