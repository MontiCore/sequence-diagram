/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd4development._cocos;

import de.monticore.lang.sd4development._ast.ASTSDNew;
import de.monticore.lang.sdbasis._ast.ASTSDObject;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSDObjectCoCo;
import de.se_rwth.commons.logging.Log;

public class ConstructorObjectNameNamingConventionCoco implements SD4DevelopmentASTSDNewCoCo {

  static final String MESSAGE_WARNING_LOWER_CASE = ConstructorObjectNameNamingConventionCoco.class.getSimpleName() + ": "
          + "Object declaration introduces an object with name %s"
          + " which should be written lower case by convention";

  static final String MESSAGE_WARNING_NOT_EMPTY = ConstructorObjectNameNamingConventionCoco.class.getSimpleName() + ": "
          + "Object declaration introduces an object with an empty name"
          + " which should either not be empty or not set";

  @Override
  public void check(ASTSDNew object) {
    String name = object.getName();
    if (name.isEmpty()) {
      Log.warn(MESSAGE_WARNING_NOT_EMPTY);
    }
    if (Character.isUpperCase(name.charAt(0))) {
      Log.warn(String.format(MESSAGE_WARNING_LOWER_CASE, name));
    }
  }
}
