/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.sdbasis._ast.ASTSDObject;
import de.se_rwth.commons.logging.Log;

public class ObjectNameNamingConventionCoco implements SDBasisASTSDObjectCoCo {

  static final String MESSAGE_WARNING_LOWER_CASE = ObjectNameNamingConventionCoco.class.getSimpleName() + ": "
          + "Object declaration introduces an object with name %s"
          + " which should be written lower case by convention";

  static final String MESSAGE_WARNING_NOT_EMPTY = ObjectNameNamingConventionCoco.class.getSimpleName() + ": "
          + "Object declaration introduces an object with an empty name"
          + " which should either not be empty or not set";

  @Override
  public void check(ASTSDObject object) {
    String name = object.getName();
    if (name.isEmpty()) {
      Log.warn(MESSAGE_WARNING_NOT_EMPTY);
    }
    if (Character.isUpperCase(name.charAt(0))) {
      Log.warn(String.format(MESSAGE_WARNING_LOWER_CASE, name));
    }
  }
}
