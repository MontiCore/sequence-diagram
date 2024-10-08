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

  private final FullSDBasisSynthesizer synthesizer = new FullSDBasisSynthesizer();

  @Override
  public ISD4DevelopmentArtifactScope createFromAST(ASTSDArtifact rootNode) {
    ISD4DevelopmentArtifactScope artifactScope = super.createFromAST(rootNode);
    String packageDeclaration = rootNode.isPresentPackageDeclaration() ? rootNode.getPackageDeclaration().getQName() : "";
    artifactScope.setPackageName(packageDeclaration);
    return artifactScope;
  }

  @Override
  public void endVisit(ASTSDNew node)  {
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
  public void endVisit(ASTSDVariableDeclaration node) {
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
