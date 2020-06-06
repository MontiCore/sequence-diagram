/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdcore._cocos;

import de.monticore.lang.sdcore._ast.ASTObject;
import de.se_rwth.commons.logging.Log;

public class ObjectNameNamingConventionCoco implements SDCoreASTObjectCoCo {
  static final String MESSAGE_WARNING_LOWER_CASE = "Objectdeclaration introduces an object with name %s"
          + " which should be written lower case by convention";
  static final String MESSAGE_WARNING_NOT_EMPTY = "Objectdeclaration introduces an object with an empty name"
          + " which should either not be empty or not set";
  @Override
  public void check(ASTObject object) {
    if(object.isPresentName()) {
      String name = object.getName();
      if(name.isEmpty()) {
        Log.warn(MESSAGE_WARNING_NOT_EMPTY);
      }
      if (Character.isUpperCase(name.charAt(0))) {
        Log.warn(String.format(MESSAGE_WARNING_LOWER_CASE, name));
      }
    }
  }
}
