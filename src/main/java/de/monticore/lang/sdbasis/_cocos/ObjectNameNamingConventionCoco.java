/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.sdbasis._ast.ASTSDObject;
import de.se_rwth.commons.logging.Log;

/**
 * Checks the naming convention for objects in a sequence diagram, i.e.,
 * every object name is not empty and starts with a lower case
 */
public class ObjectNameNamingConventionCoco implements SDBasisASTSDObjectCoCo {

  private static final String MESSAGE_WARNING_LOWER_CASE = "0xB0017: "
          + "Object declaration introduces an object with name %s"
          + " which should be written lower case by convention";

  private static final String MESSAGE_WARNING_NOT_EMPTY = "0xB0025: "
          + "Object declaration introduces an object with an empty name"
          + " which should not be empty";

  @Override
  public void check(ASTSDObject object) {
    String name = object.getName();
    if (name.isEmpty()) {
      Log.warn(MESSAGE_WARNING_NOT_EMPTY);
    }
    else if (!Character.isLowerCase(name.charAt(0))) {
      Log.warn(String.format(MESSAGE_WARNING_LOWER_CASE, name));
    }
  }
}
