/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd4development._cocos;

import com.google.common.collect.Iterables;
import de.monticore.ast.ASTNode;
import de.monticore.lang.sd4development._ast.ASTSDNew;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentVisitor;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis._ast.ASTSDObjectSource;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSDArtifactCoCo;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.SymTypeExpressionFactory;
import de.monticore.types.check.TypeCheck;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.monticore.types.mcbasictypes._ast.ASTMCObjectType;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.se_rwth.commons.logging.Log;

import java.util.*;

import static de.monticore.lang.util.FQNameCalculator.calcFQNameCandidates;

/**
 * Checks if the type declared for an object instantiated with an SDNew interaction and the
 * object's initialization type are compatible.
 */
public class CorrectObjectConstructionTypesCoco implements SDBasisASTSDArtifactCoCo {

  private static final String INCOMPATIBLE_TYPES = "0xB0009: " + "%s and %s are incompatible types. Use the same type or a subtype of %s as the initialization type";
  private static final String TYPE_DEFINED_MUTLIPLE_TIMES = "0xB0032: Type '%s' is defined more than once.";
  private static final String TYPE_USED_BUT_UNDEFINED = "0xB0028: Type '%s' is used but not defined.";

  private List<ASTMCImportStatement> imports = new ArrayList<>();
  private ASTMCQualifiedName packageDeclaration;
  private final MCBasicTypesPrettyPrinter prettyPrinter;

  public CorrectObjectConstructionTypesCoco() {
    this.prettyPrinter = new MCBasicTypesPrettyPrinter(new IndentPrinter());
  }

  @Override
  public void check(ASTSDArtifact node) {
    this.imports.addAll(node.getMCImportStatementList());
    this.packageDeclaration = node.getPackageDeclaration();

    Deque<ASTNode> toProcess = new ArrayDeque<>();
    toProcess.addAll(node.get_Children());
    while(!toProcess.isEmpty()) {
      ASTNode current = toProcess.pop();
      if(current instanceof ASTSDNew) {
        check((ASTSDNew) current);
      }
      toProcess.addAll(current.get_Children());
    }
  }

  public void check(ASTSDNew node) {

    ASTMCObjectType declType = node.getDeclarationType();
    ASTMCObjectType initType = node.getInitializationType();

    String declTypeName = declType.printType(prettyPrinter);
    String initTypeName = initType.printType(prettyPrinter);

    TypeSymbol declTypeSymbol = resolveTypeSymbol(node, declTypeName);
    TypeSymbol initTypeSymbol = resolveTypeSymbol(node, initTypeName);

    SymTypeExpression declTypeExpression = SymTypeExpressionFactory.createTypeExpression(declTypeSymbol);
    SymTypeExpression initTypeExpression = SymTypeExpressionFactory.createTypeExpression(initTypeSymbol);

    if (!TypeCheck.compatible(initTypeExpression, declTypeExpression)) {
      Log.error(String.format(INCOMPATIBLE_TYPES, declTypeName, initTypeName, declTypeName));
    }
  }

  private TypeSymbol resolveTypeSymbol(ASTSDNew node, String typeName) {
    Set<TypeSymbol> typeSymbols = new HashSet<>();
    for (String fqNameCandidate : calcFQNameCandidates(imports, packageDeclaration, typeName)) {
      typeSymbols.addAll(node.getEnclosingScope().resolveTypeMany(fqNameCandidate));
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
