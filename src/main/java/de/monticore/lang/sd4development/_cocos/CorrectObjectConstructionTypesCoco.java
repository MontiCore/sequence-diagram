/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd4development._cocos;

import com.google.common.collect.Iterables;
import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sd4development._ast.ASTSDNew;
import de.monticore.lang.sd4development._symboltable.SD4DevelopmentScope;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentTraverser;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSDArtifactCoCo;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.SymTypeExpressionFactory;
import de.monticore.types.check.TypeCheck;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.monticore.types.mcbasictypes._ast.ASTMCObjectType;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static de.monticore.lang.util.FQNameCalculator.calcFQNameCandidates;

/**
 * Checks if the type declared for an object instantiated with an SDNew interaction and the
 * object's initialization type are compatible.
 */
public class CorrectObjectConstructionTypesCoco implements SDBasisASTSDArtifactCoCo {

  private static final String INCOMPATIBLE_TYPES = "0xB0009: " + "%s and %s are incompatible types. Use the same type or a subtype of %s as the initialization type";
  private static final String TYPE_DEFINED_MUTLIPLE_TIMES = "0xB0103: Type '%s' is defined more than once.";
  private static final String TYPE_USED_BUT_UNDEFINED = "0xB0104: Type '%s' is used but not defined.";

  private List<ASTMCImportStatement> imports = new ArrayList<>();
  private ASTMCQualifiedName packageDeclaration;

  @Override
  public void check(ASTSDArtifact node) {
    this.imports.addAll(node.getMCImportStatementList());
    this.packageDeclaration = node.isPresentPackageDeclaration() ? node.getPackageDeclaration() : SD4DevelopmentMill.mCQualifiedNameBuilder().build();

    SDNewCollector c = new SDNewCollector();
    SD4DevelopmentTraverser t = SD4DevelopmentMill.traverser();
    t.setSD4DevelopmentHandler(c);
    t.add4SD4Development(c);
    c.setTraverser(t);
    node.accept(t);

    c.getResult().forEach(this::check);
  }

  public void check(ASTSDNew node) {

    ASTMCObjectType declType = node.getDeclarationType();
    ASTMCObjectType initType = node.getInitializationType();

    String declTypeName = SD4DevelopmentMill.prettyPrint(declType, false);
    String initTypeName = SD4DevelopmentMill.prettyPrint(initType, false);

    TypeSymbol declTypeSymbol = resolveOOTypeSymbol(node, declTypeName);
    TypeSymbol initTypeSymbol = resolveOOTypeSymbol(node, initTypeName);

    SymTypeExpression declTypeExpression = SymTypeExpressionFactory.createTypeExpression(declTypeSymbol);
    SymTypeExpression initTypeExpression = SymTypeExpressionFactory.createTypeExpression(initTypeSymbol);

    if (!TypeCheck.compatible(initTypeExpression, declTypeExpression)) {
      Log.error(String.format(INCOMPATIBLE_TYPES, declTypeName, initTypeName, declTypeName));
    }
  }

  private TypeSymbol resolveOOTypeSymbol(ASTSDNew node, String typeName) {
    Set<TypeSymbol> typeSymbols = new HashSet<>();
    for (String fqNameCandidate : calcFQNameCandidates(imports, packageDeclaration, typeName)) {
      SD4DevelopmentScope scope = (SD4DevelopmentScope) node.getEnclosingScope();
      typeSymbols.addAll(scope.resolveOOTypeMany(fqNameCandidate));
    }

    if (typeSymbols.isEmpty()) {
      Log.error(String.format(TYPE_USED_BUT_UNDEFINED, typeName), node.get_SourcePositionStart());
    }
    else if (typeSymbols.size() > 1) {
      Log.error(String.format(TYPE_DEFINED_MUTLIPLE_TIMES, typeName), node.get_SourcePositionStart());
    }

    return Iterables.getFirst(typeSymbols, null);
  }
}
