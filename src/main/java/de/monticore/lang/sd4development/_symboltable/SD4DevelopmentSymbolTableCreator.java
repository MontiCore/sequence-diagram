package de.monticore.lang.sd4development._symboltable;

import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.symboltable.ImportStatement;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class SD4DevelopmentSymbolTableCreator extends SD4DevelopmentSymbolTableCreatorTOP {

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
    artifactScope.setImportList(getImports(rootNode));
    return artifactScope;
  }

  private List<ImportStatement> getImports(ASTSDArtifact ast) {
    return ast.getMCImportStatementList().stream().map(e -> new ImportStatement(e.getQName(), e.isStar())).collect(Collectors.toList());
  }
}
