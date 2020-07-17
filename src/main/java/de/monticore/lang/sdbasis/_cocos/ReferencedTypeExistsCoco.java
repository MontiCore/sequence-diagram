package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.sd4development._symboltable.SD4DevelopmentScope;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCType;
import de.monticore.types.mcbasictypes._cocos.MCBasicTypesASTMCTypeCoCo;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.se_rwth.commons.logging.Log;

/**
 * Checks if used types are defined.
 */
public class ReferencedTypeExistsCoco implements MCBasicTypesASTMCTypeCoCo {

  private static final String MESSAGE = "0xB0028: " +
    "Type '%s' is used but not defined.";

  private final MCBasicTypesPrettyPrinter pp = new MCBasicTypesPrettyPrinter(new IndentPrinter());

  @Override
  public void check(ASTMCType node) {
    if (node.getEnclosingScope() instanceof SD4DevelopmentScope) {
      SD4DevelopmentScope scope = (SD4DevelopmentScope) node.getEnclosingScope();
      String type = node.printType(pp);
      if (!scope.resolveType(type).isPresent() && !scope.resolveOOType(type).isPresent()) {
        Log.error(String.format(MESSAGE, type));
      }
    }
  }
}
