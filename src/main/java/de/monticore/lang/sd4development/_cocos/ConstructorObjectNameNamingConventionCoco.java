/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd4development._cocos;

import de.monticore.lang.sd4development._ast.ASTSDNew;
import de.monticore.lang.sdbasis._ast.ASTSDObject;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSDObjectCoCo;
import de.se_rwth.commons.logging.Log;

/**
 * Checks if naming conventions of SDNew constructs are fulfilled, i.e.,
 * the name is not empty and starts with a lower case
 */
public class ConstructorObjectNameNamingConventionCoco implements SD4DevelopmentASTSDNewCoCo {

  private static final String MESSAGE_WARNING_LOWER_CASE = "0xS0005: "
          + "Object declaration introduces an object with name %s"
          + " which should be written lower case by convention.";

  private static final String MESSAGE_WARNING_NOT_EMPTY = "0xS0006: "
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
