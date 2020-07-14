/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd4development._cocos;

import de.monticore.lang.sd4development._ast.ASTSDNew;
import de.monticore.lang.sdbasis.types.DeriveSymTypeOfSDBasis;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.check.SymTypeExpression;
import de.monticore.types.check.TypeCheck;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;

/**
 * Checks if the declared types of a SDNew construct, i.e., the declaration and the
 * initialization type are compatible.
 */
public class CorrectObjectConstructionTypesCoco implements SD4DevelopmentASTSDNewCoCo {

  private static final String MESSAGE_ERROR_DECLARATION_TYPE_MISSING = "0xB0007: " +
    "SymType of declaration type '%s' is missing. Can't check whether types are compatible.";

  private static final String MESSAGE_ERROR_INITIALIZATION_TYPE_MISSING = "0xB0008: " +
    "SymType of initialization type '%s' is missing. Can't check whether types are compatible.";

  private static final String MESSAGE_ERROR_INCOMPATIBLE_TYPES = "0xB0009: " +
    "%s and %s are incompatible types. Use the same type or a subtype of %s";

  private final DeriveSymTypeOfSDBasis deriveSymTypeOfSDBasis;

  private final MCBasicTypesPrettyPrinter prettyPrinter;

  public CorrectObjectConstructionTypesCoco() {
    this.deriveSymTypeOfSDBasis = new DeriveSymTypeOfSDBasis();
    this.prettyPrinter = new MCBasicTypesPrettyPrinter(new IndentPrinter());
  }

  @Override
  public void check(ASTSDNew node) {
    // used for potential error messages
    String declarationTypeStr = node.getDeclarationType().printType(prettyPrinter);
    String initializationTypeStr = node.getInitializationType().printType(prettyPrinter);

    Optional<SymTypeExpression> declarationType = deriveSymTypeOfSDBasis.calculateType(node.getDeclarationType());
    Optional<SymTypeExpression> initializationType = deriveSymTypeOfSDBasis.calculateType(node.getInitializationType());

    if (!declarationType.isPresent()) {
      Log.warn(String.format(MESSAGE_ERROR_DECLARATION_TYPE_MISSING, declarationTypeStr));
      return;
    }

    if (!initializationType.isPresent()) {
      Log.warn(String.format(MESSAGE_ERROR_INITIALIZATION_TYPE_MISSING, initializationTypeStr));
      return;
    }

    if (!TypeCheck.compatible(initializationType.get(), declarationType.get())) {
      Log.error(String.format(MESSAGE_ERROR_INCOMPATIBLE_TYPES, declarationTypeStr, initializationTypeStr, declarationTypeStr));
    }
  }
}
