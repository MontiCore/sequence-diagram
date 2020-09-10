package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.sd4development._symboltable.SD4DevelopmentScope;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentInheritanceVisitor;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentVisitor;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.symbols.basicsymbols._symboltable.TypeSymbol;
import de.monticore.symbols.basicsymbols._symboltable.VariableSymbol;
import de.monticore.types.check.TypeCheck;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.monticore.types.mcbasictypes._cocos.MCBasicTypesASTMCTypeCoCo;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;
import java.util.List;

import static de.monticore.lang.util.FQNameCalculator.calcFQNameCandidates;

/**
 * Checks if used types are defined.
 */
public class ReferencedTypeExistsCoco implements SDBasisASTSDArtifactCoCo, SD4DevelopmentInheritanceVisitor {

  private static final String USED_BUT_UNDEFINED = "0xB0028: Type '%s' is used but not defined.";

  private static final String DEFINED_MUTLIPLE_TIMES = "0xB0031: Type '%s' is defined more than once.";

  private final MCBasicTypesPrettyPrinter pp = new MCBasicTypesPrettyPrinter(new IndentPrinter());

  private List<ASTMCImportStatement> imports = new ArrayList<>();
  private ASTMCQualifiedName packageDeclaration;

  @Override
  public void check(ASTSDArtifact node) {
    this.imports.addAll(node.getMCImportStatementList());
    this.packageDeclaration = node.getPackageDeclaration();
    node.accept(this);
  }

  @Override
  public void visit(ASTMCType node) {

    if (node.getEnclosingScope() instanceof SD4DevelopmentScope) {
      SD4DevelopmentScope scope = (SD4DevelopmentScope) node.getEnclosingScope();
      String type = node.printType(pp);

      List<TypeSymbol> typeSymbols = new ArrayList<>();

      for (String fqNameCandidate : calcFQNameCandidates(imports, packageDeclaration, type)) {
        typeSymbols.addAll(scope.resolveTypeMany(fqNameCandidate));
      }

      if (typeSymbols.isEmpty()) {
        Log.error(String.format(USED_BUT_UNDEFINED, type), node.get_SourcePositionStart());
        return;
      }
      else if (typeSymbols.size() > 1) {
        Log.error(String.format(DEFINED_MUTLIPLE_TIMES, type), node.get_SourcePositionStart());
      }

    }
  }

}
