/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development._symboltable;

import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.symboltable.ImportStatement;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;

import java.util.ArrayList;
import java.util.List;

public class SD4DevelopmentScopesGenitor extends SD4DevelopmentScopesGenitorTOP {

  @Override
  public ISD4DevelopmentArtifactScope createFromAST(ASTSDArtifact rootNode) {
    ISD4DevelopmentArtifactScope artifactScope = super.createFromAST(rootNode);
    String packageDeclaration = rootNode.isPresentPackageDeclaration() ? rootNode.getPackageDeclaration().getQName() : "";
    artifactScope.setPackageName(packageDeclaration);

    List<ImportStatement> imports = new ArrayList<>();
    for (ASTMCImportStatement importStatement : rootNode.getMCImportStatementList()) {
      imports.add(new ImportStatement(importStatement.getQName(), importStatement.isStar()));
    }
    artifactScope.setImportsList(imports);

    return artifactScope;
  }
}
