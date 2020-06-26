package de.monticore.lang.sd4development._symboltable;

import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis._ast.ASTSDObject;
import de.monticore.lang.sdbasis._symboltable.ISDBasisScope;
import de.monticore.lang.sdbasis._symboltable.SDBasisArtifactScope;
import de.monticore.lang.sdbasis._symboltable.SDBasisSymbolTableCreatorTOP;
import de.monticore.lang.sdbasis.types.DeriveSymTypeOfSDBasis;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symboltable.ImportStatement;
import de.monticore.types.basictypesymbols._symboltable.VariableSymbol;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.se_rwth.commons.logging.Log;

import java.util.Deque;
import java.util.List;
import java.util.Optional;
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
