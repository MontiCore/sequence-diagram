/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development._cocos;

import com.google.common.collect.Iterables;
import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.expressions.expressionsbasis._ast.ASTNameExpression;
import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sd4development._ast.ASTSDCall;
import de.monticore.lang.sd4development._symboltable.SD4DevelopmentScope;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentTraverser;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis._ast.ASTSDObjectTarget;
import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSDArtifactCoCo;
import de.monticore.lang.sdbasis.types.FullSDBasisDeriver;
import de.monticore.symbols.basicsymbols._symboltable.FunctionSymbol;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.TypeCheck;
import de.monticore.types.check.TypeCheckResult;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.se_rwth.commons.logging.Log;

import java.util.*;

import static de.monticore.lang.util.FQNameCalculator.calcFQNameCandidates;

/**
 * Checks if a method action is valid, i.e., whether a method with a
 * corresponding signature is defined for the type of the target of
 * the interaction. The name of the method as well as the number
 * and types of method parameters must be equal.
 */
public class MethodActionValidCoco implements SDBasisASTSDArtifactCoCo {

  private static final String MESSAGE = "0xB0012: '%s' type does not contain method '%s'.";
  private static final String TYPE_DEFINED_MUTLIPLE_TIMES = "0xB0033: Type '%s' is defined more than once.";
  private static final String TYPE_USED_BUT_UNDEFINED = "0xB0034: Type '%s' is used but not defined.";

  private final FullSDBasisDeriver deriver;
  private final List<ASTMCImportStatement> imports = new ArrayList<>();
  private ASTMCQualifiedName packageDeclaration;

  public MethodActionValidCoco() {
    this.deriver = new FullSDBasisDeriver();
  }

  @Override
  public void check(ASTSDArtifact node) {
    this.imports.addAll(node.getMCImportStatementList());
    this.packageDeclaration = node.isPresentPackageDeclaration() ? node.getPackageDeclaration() : SD4DevelopmentMill.mCQualifiedNameBuilder().build();

    SDSendMessageCollector c = new SDSendMessageCollector();
    SD4DevelopmentTraverser t = SD4DevelopmentMill.traverser();
    t.setSDBasisHandler(c);
    t.add4SDBasis(c);
    c.setTraverser(t);
    node.accept(t);

    c.getResult().forEach(this::check);
  }

  public void check(ASTSDSendMessage node) {
    if (isSDCallAndObjectTargetPresent(node)) {
      ASTSDCall call = (ASTSDCall) node.getSDAction();

      String targetObjectName = ((ASTSDObjectTarget) node.getSDTarget()).getName();

      Set<VariableSymbol> varSymbols = new HashSet<>();
      for (String fqNameCandidate : calcFQNameCandidates(imports, packageDeclaration, targetObjectName)) {
        varSymbols.addAll(node.getEnclosingScope().resolveVariableMany(fqNameCandidate));
      }
      if (varSymbols.size() == 1) {
        // in case varSymbols.size() != 1, another CoCo reports an error
        VariableSymbol targetObjectVarSymbol = Iterables.getFirst(varSymbols, null);
        String targetTypeName = targetObjectVarSymbol.getType().getTypeInfo().getName();

        // compute and store all functions defined by the type and its super types
        TypeSymbol targetTypeSymbol = resolveOOTypeSymbol(node, targetTypeName);
        List<FunctionSymbol> functionSymbols = new ArrayList<>();
        Deque<TypeSymbol> superTypesToProcess = new ArrayDeque<>();

        if (targetTypeSymbol != null) {
          superTypesToProcess.add(targetTypeSymbol);
          Set<TypeSymbol> processed = new HashSet<>();
          while (!superTypesToProcess.isEmpty()) {
            TypeSymbol cur = superTypesToProcess.pop();
            processed.add(cur);
            functionSymbols.addAll(cur.getFunctionList());
            for (SymTypeExpression superType : cur.getSuperTypesList()) {
              TypeSymbol superTypeInfo = superType.getTypeInfo();
              if (!processed.contains(superTypeInfo)) {
                superTypesToProcess.add(superTypeInfo);
              }
            }
          }
        }

        // check if used function is defined in the type or its supertypes
        for (FunctionSymbol methodSymbol : functionSymbols) {
          if (methodAndCallMatch(methodSymbol, call)) {
            return;
          }
        }
        Log.warn(String.format(MESSAGE, targetTypeName, call.getName()));
      }
    }
  }

  private boolean isSDCallAndObjectTargetPresent(ASTSDSendMessage node) {
    return node.getSDAction() instanceof ASTSDCall && node.isPresentSDTarget() && node.getSDTarget() instanceof ASTSDObjectTarget;
  }

  /**
   * This method checks, if a method invocation, i.e., the call, matches with a given MethodSymbol, i.e., the declaration
   *
   * @param methodSymbol the method declaration
   * @param call         the invocation of a method
   * @return true, if the signature of call corresponds with the signature of the given method symbol
   */
  private boolean methodAndCallMatch(FunctionSymbol methodSymbol, ASTSDCall call) {
    if (!isSameParameterSize(methodSymbol, call)) {
      return false;
    }
    if (!methodNameMatchesFunctionSymbolName(methodSymbol, call)) {
      return false;
    }

    for (int i = 0; i < methodSymbol.getParameterList().size(); i++) {
      SymTypeExpression methodParameterType = methodSymbol.getParameterList().get(i).getType();
      ASTExpression callArgument = call.getArguments().getExpression(i);
      TypeCheckResult callArgumentType = deriver.deriveType(callArgument);

      if (!callArgumentType.isPresentResult() && !(callArgument instanceof ASTNameExpression)) {
        return false;
      }

      if (callArgumentType.isPresentResult() && !TypeCheck.compatible(methodParameterType, callArgumentType.getResult())) {
        return false;
      }
    }
    return true;
  }

  private boolean methodNameMatchesFunctionSymbolName(FunctionSymbol methodSymbol, ASTSDCall call) {
    String usedMethodName = call.getName();
    return usedMethodName.equals(methodSymbol.getName());
  }

  private boolean isSameParameterSize(FunctionSymbol methodSymbol, ASTSDCall call) {
    return methodSymbol.getParameterList().size() == call.getArguments().getExpressionList().size();
  }

  private TypeSymbol resolveOOTypeSymbol(ASTSDSendMessage node, String typeName) {
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
