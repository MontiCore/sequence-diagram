/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._symboltable;

import com.google.common.base.Preconditions;
import de.monticore.lang.sd4components.SD4ComponentsMill;
import de.monticore.lang.sd4components._ast.ASTSDComponent;
import de.monticore.lang.sd4components._ast.ASTSDPort;
import de.monticore.lang.sd4components._ast.ASTSDVariableDeclaration;
import de.monticore.lang.sd4components._ast.Expression2SubcomponentArgumentAdapter;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.symbols.compsymbols._symboltable.ComponentTypeSymbol;
import de.monticore.symbols.compsymbols._symboltable.PortSymbol;
import de.monticore.symbols.compsymbols._symboltable.Subcomponent2VariableAdapter;
import de.monticore.symbols.compsymbols._symboltable.SubcomponentSymbol;
import de.monticore.symboltable.ImportStatement;
import de.monticore.types.check.CompKindExpression;
import de.monticore.types.check.FullSynthesizeCompKindFromMCBasicTypes;
import de.monticore.types.check.ISynthesizeComponent;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.monticore.types3.TypeCheck3;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SD4ComponentsScopesGenitor extends SD4ComponentsScopesGenitorTOP {

  protected final ISynthesizeComponent componentSynthesizer;

  public SD4ComponentsScopesGenitor(ISynthesizeComponent componentSynthesizer) {
    this.componentSynthesizer = componentSynthesizer;
  }

  public SD4ComponentsScopesGenitor() {
    this(new FullSynthesizeCompKindFromMCBasicTypes());
  }

  @Override
  public ISD4ComponentsArtifactScope createFromAST(ASTSDArtifact rootNode) {
    Preconditions.checkNotNull(rootNode,
        "0xA7004x34638 Error by creating of the SD4ComponentsScopesGenitor symbol table: top ast node is null");

    String packageDeclaration = rootNode.isPresentPackageDeclaration() ? rootNode.getPackageDeclaration().getQName() : "";
    List<ImportStatement> imports = new ArrayList<>();
    for (ASTMCImportStatement importStatement : rootNode.getMCImportStatementList()) {
      imports.add(new ImportStatement(importStatement.getQName(), importStatement.isStar()));
    }

    ISD4ComponentsArtifactScope artifactScope = de.monticore.lang.sd4components.SD4ComponentsMill.artifactScope();
    artifactScope.setPackageName(packageDeclaration);
    artifactScope.setImportsList(imports);
    artifactScope.setAstNode(rootNode);
    putOnStack(artifactScope);
    initArtifactScopeHP1(artifactScope);
    rootNode.accept(getTraverser());
    initArtifactScopeHP2(artifactScope);
    scopeStack.remove(artifactScope);
    return artifactScope;
  }

  @Override
  public void visit(ASTSDComponent node) {
    SubcomponentSymbol symbol =
        SD4ComponentsMill.subcomponentSymbolBuilder().setName(node.getName()).build();
    Subcomponent2VariableAdapter adapter = new Subcomponent2VariableAdapter(symbol);
    if (getCurrentScope().isPresent()) {
      getCurrentScope().get().add(symbol);
    }
    else {
      Log.warn("0xA5021x48901 Symbol cannot be added to current scope, since no scope exists.");
    }

    // ast -> symbol
    node.setSymbol(adapter);
    node.setEnclosingScope(symbol.getEnclosingScope());

    initVariableHP1(node.getSymbol());
  }

  @Override
  public void endVisit(ASTSDComponent node) {
    super.endVisit(node);
    if (!node.isPresentMCObjectType()) return;

    Optional<CompKindExpression> typeExpression =
        componentSynthesizer.synthesize(node.getMCObjectType());
    if (typeExpression.isPresent()) {
      ((Subcomponent2VariableAdapter) node.getSymbol()).getAdaptee().setType(typeExpression.get());
      if (node.isPresentArguments()) {
        typeExpression.get().addArgument(node.getArguments().getExpressionList().stream()
            .map(Expression2SubcomponentArgumentAdapter::new).collect(Collectors.toList()));
      }
      typeExpression.get().bindParams();
    }
  }

  @Override
  public void endVisit(ASTSDPort node) {
    if (node.isPresentNameSymbol() &&
        node.getNameSymbol().isTypePresent() &&
        node.getNameSymbol().getType().getTypeInfo() instanceof ComponentTypeSymbol typeSymbol) {
      Optional<PortSymbol> port = typeSymbol.getPort(node.getPort());
      port.ifPresent(node::setPortSymbol);
    }
  }

  @Override
  public void endVisit(ASTSDVariableDeclaration node) {
    VariableSymbol symbol = node.getSymbol();

    final SymTypeExpression typeResult = TypeCheck3.symTypeFromAST(node.getMCType());
    if (!typeResult.isObscureType()) {
      symbol.setType(typeResult);
    }
  }
}
