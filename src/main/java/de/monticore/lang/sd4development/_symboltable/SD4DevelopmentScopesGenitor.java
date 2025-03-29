/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development._symboltable;

import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sd4development._ast.ASTSDNew;
import de.monticore.lang.sd4development._ast.ASTSDVariableDeclaration;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis.types.FullSDBasisSynthesizer;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.types.check.TypeCheckResult;
import de.se_rwth.commons.logging.Log;

public class SD4DevelopmentScopesGenitor extends SD4DevelopmentScopesGenitorTOP {

  @Override
  public ISD4DevelopmentArtifactScope createFromAST(ASTSDArtifact rootNode) {
    ISD4DevelopmentArtifactScope artifactScope = super.createFromAST(rootNode);
    String packageDeclaration = rootNode.isPresentPackageDeclaration() ? rootNode.getPackageDeclaration().getQName() : "";
    artifactScope.setPackageName(packageDeclaration);
    return artifactScope;
  }
}
