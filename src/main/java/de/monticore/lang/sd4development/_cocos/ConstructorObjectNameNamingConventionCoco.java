/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd4development._cocos;

import de.monticore.lang.sd4development._ast.ASTSDNew;
import de.se_rwth.commons.logging.Log;

/**
 * Checks if naming conventions for objects constructed with SDNew
 * are satisfied, i.e., the name is not empty and starts
 * with a lower case letter.
 */
public class ConstructorObjectNameNamingConventionCoco implements SD4DevelopmentASTSDNewCoCo {

  private static final String MESSAGE_WARNING_LOWER_CASE = "0xB0005: "
          + "Object declaration introduces an object with name %s"
          + " which should be written lower case by convention.";

  private static final String MESSAGE_WARNING_NOT_EMPTY = "0xB0006: "
          + "Object declaration introduces an object with an empty name"
          + " which should not be empty.";

  @Override
  public void check(ASTSDNew object) {
    String name = object.getName();
    if (name.isEmpty()) {
      Log.warn(MESSAGE_WARNING_NOT_EMPTY);
    }
    else if (!Character.isLowerCase(name.charAt(0))) {
      Log.warn(String.format(MESSAGE_WARNING_LOWER_CASE, name));
    }
  }
}
