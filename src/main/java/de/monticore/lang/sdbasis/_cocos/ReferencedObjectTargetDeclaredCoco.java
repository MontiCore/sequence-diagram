/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sdbasis._cocos;

import com.google.common.collect.Iterables;
import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentTraverser;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis._ast.ASTSDObjectTarget;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static de.monticore.lang.util.FQNameCalculator.calcFQNameCandidates;

/**
 * Checks if used target objects are declared before they are used.
 */
public class ReferencedObjectTargetDeclaredCoco implements SDBasisASTSDArtifactCoCo {

  private static final String NOT_DECLARED = "0xB0020: " + "Object '%s' is used, but not declared.";

  private static final String DECLARED_MULTIPLE_TIMES = "0xB0029: " + "Object '%s' is used and declared multiple times.";

  private static final String NOT_DECLARED_BEFORE = "0xB0027: " + "Object '%s' is used before it is declared.";

  private final List<ASTMCImportStatement> imports = new ArrayList<>();
  private ASTMCQualifiedName packageDeclaration;

  @Override
  public void check(ASTSDArtifact node) {
    this.imports.addAll(node.getMCImportStatementList());
    this.packageDeclaration = node.isPresentPackageDeclaration() ? node.getPackageDeclaration() : SD4DevelopmentMill.mCQualifiedNameBuilder().build();

    SDObjectTargetCollector c = new SDObjectTargetCollector();
    SD4DevelopmentTraverser t = SD4DevelopmentMill.traverser();
    t.setSDBasisHandler(c);
    t.add4SDBasis(c);
    c.setTraverser(t);
    node.accept(t);

    c.getResult().forEach(this::check);
  }

  public void check(ASTSDObjectTarget node) {
    Set<VariableSymbol> varSymbols = new HashSet<>();

    for (String fqNameCandidate : calcFQNameCandidates(imports, packageDeclaration, node.getName())) {
      varSymbols.addAll(node.getEnclosingScope().resolveVariableMany(fqNameCandidate));
    }

    if (varSymbols.isEmpty()) {
      Log.error(String.format(NOT_DECLARED, node.getName()), node.get_SourcePositionStart());
    }
    else if (varSymbols.size() > 1) {
      Log.error(String.format(DECLARED_MULTIPLE_TIMES, node.getName()), node.get_SourcePositionStart());
    }
    else {
      VariableSymbol singleElement = Iterables.getFirst(varSymbols, null);
      if (singleElement.isPresentAstNode() && singleElement.getAstNode().get_SourcePositionEnd().compareTo(node.get_SourcePositionStart()) > 0) {
        Log.error(String.format(NOT_DECLARED_BEFORE, node.getName()), node.get_SourcePositionStart());
      }
    }
  }

}
