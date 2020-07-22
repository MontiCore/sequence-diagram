package de.monticore.lang.sd4development._symboltable;

import de.monticore.lang.sd4development._ast.ASTEDeclaration;
import de.monticore.lang.sd4development._ast.ASTSDNew;
import de.monticore.lang.sd4development._ast.ASTSDVariableDeclaration;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis.types.DeriveSymTypeOfSDBasis;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.symboltable.ImportStatement;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.se_rwth.commons.logging.Log;

import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SD4DevelopmentSymbolTableCreator extends SD4DevelopmentSymbolTableCreatorTOP {

  private final DeriveSymTypeOfSDBasis typeChecker = new DeriveSymTypeOfSDBasis();

  private final MCBasicTypesPrettyPrinter prettyPrinter = new MCBasicTypesPrettyPrinter(new IndentPrinter());

  public SD4DevelopmentSymbolTableCreator(ISD4DevelopmentScope enclosingScope) {
    super(enclosingScope);
  }

  public SD4DevelopmentSymbolTableCreator(Deque<? extends ISD4DevelopmentScope> scopeStack) {
    super(scopeStack);
  }

  @Override
  public SD4DevelopmentArtifactScope createFromAST(ASTSDArtifact rootNode) {
    SD4DevelopmentArtifactScope artifactScope = super.createFromAST(rootNode);
    artifactScope.setPackageName(rootNode.getPackageDeclaration().getQName());
    artifactScope.setImportsList(getImports(rootNode));
    return artifactScope;
  }

  private List<ImportStatement> getImports(ASTSDArtifact ast) {
    return ast.getMCImportStatementsList().stream().map(e -> new ImportStatement(e.getQName(), e.isStar())).collect(Collectors.toList());
  }

  @Override
  public  void visit(ASTSDNew node)  {
    VariableSymbol symbol = create_SDNew(node);
    symbol.setEnclosingScope(getCurrentScope().get());
    addToScopeAndLinkWithNode(symbol, node);
    initialize_SDNew(symbol, node);
  }

  @Override
  protected void initialize_SDNew(VariableSymbol symbol, ASTSDNew ast) {
    ast.getDeclarationType().setEnclosingScope(ast.getEnclosingScope());
    final Optional<SymTypeExpression> typeResult = typeChecker.calculateType(ast.getDeclarationType());
    if (!typeResult.isPresent()) {
      Log.error(String.format("0xA0000: The type (%s) of the attribute (%s) could not be calculated",
              prettyPrinter.prettyprint(ast.getDeclarationType()),
              ast.getName()));
    } else {
      symbol.setType(typeResult.get());
    }
  }

  @Override
  public void visit(ASTEDeclaration node) {
    VariableSymbol symbol = create_EDeclaration(node);
    symbol.setEnclosingScope(getCurrentScope().get());
    addToScopeAndLinkWithNode(symbol, node);
    initialize_EDeclaration(symbol, node);
  }

  @Override
  protected void initialize_EDeclaration(VariableSymbol symbol, ASTEDeclaration ast) {
    ast.getMCType().setEnclosingScope(ast.getEnclosingScope());
    final Optional<SymTypeExpression> typeResult = typeChecker.calculateType(ast.getMCType());
    if (!typeResult.isPresent()) {
      Log.error(String.format("0xA0000: The type (%s) of the attribute (%s) could not be calculated",
              prettyPrinter.prettyprint(ast.getMCType()),
              ast.getName()));
    } else {
      symbol.setType(typeResult.get());
    }
  }

  @Override
  public void visit(ASTSDVariableDeclaration node) {
    VariableSymbol symbol = create_SDVariableDeclaration(node);
    symbol.setEnclosingScope(getCurrentScope().get());
    addToScopeAndLinkWithNode(symbol, node);
    initialize_SDVariableDeclaration(symbol, node);
  }

  @Override
  protected void initialize_SDVariableDeclaration(VariableSymbol symbol, ASTSDVariableDeclaration ast) {
    ast.getMCType().setEnclosingScope(ast.getEnclosingScope());
    final Optional<SymTypeExpression> typeResult = typeChecker.calculateType(ast.getMCType());
    if (!typeResult.isPresent()) {
      Log.error(String.format("0xA0000: The type (%s) of the attribute (%s) could not be calculated",
              prettyPrinter.prettyprint(ast.getMCType()),
              ast.getName()));
    } else {
      symbol.setType(typeResult.get());
    }
  }
}
