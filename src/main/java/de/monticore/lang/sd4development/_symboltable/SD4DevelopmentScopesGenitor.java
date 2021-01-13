/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development._symboltable;

import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sd4development._ast.ASTEDeclaration;
import de.monticore.lang.sd4development._ast.ASTSDNew;
import de.monticore.lang.sd4development._ast.ASTSDVariableDeclaration;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis.types.DeriveSymTypeOfSDBasis;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.prettyprint.MCBasicTypesFullPrettyPrinter;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;

public class SD4DevelopmentScopesGenitor extends SD4DevelopmentScopesGenitorTOP {

  private final DeriveSymTypeOfSDBasis typeChecker = new DeriveSymTypeOfSDBasis(SD4DevelopmentMill.traverser());

  // used when printing error messages
  private final MCBasicTypesFullPrettyPrinter prettyPrinter = new MCBasicTypesFullPrettyPrinter(new IndentPrinter());

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

    final Optional<SymTypeExpression> typeResult = typeChecker.calculateType(node.getDeclarationType());
    if (!typeResult.isPresent()) {
      Log.error(String.format("0xB0002: The type (%s) of the object (%s) could not be calculated",
        prettyPrinter.prettyprint(node.getDeclarationType()),
        node.getName()));
    } else {
      symbol.setType(typeResult.get());
    }
  }

  @Override
  public void endVisit(ASTEDeclaration node) {
    VariableSymbol symbol = node.getSymbol();

    final Optional<SymTypeExpression> typeResult = typeChecker.calculateType(node.getMCType());
    if (!typeResult.isPresent()) {
      Log.error(String.format("0xB0003: The type (%s) of the variable (%s) could not be calculated",
        prettyPrinter.prettyprint(node.getMCType()),
        node.getName()));
    } else {
      symbol.setType(typeResult.get());
    }
  }

  @Override
  public void endVisit(ASTSDVariableDeclaration node) {
    VariableSymbol symbol = node.getSymbol();

    final Optional<SymTypeExpression> typeResult = typeChecker.calculateType(node.getMCType());
    if (!typeResult.isPresent()) {
      Log.error(String.format("0xB0004: The type (%s) of the variable (%s) could not be calculated",
        prettyPrinter.prettyprint(node.getMCType()),
        node.getName()));
    } else {
      symbol.setType(typeResult.get());
    }
  }

}
