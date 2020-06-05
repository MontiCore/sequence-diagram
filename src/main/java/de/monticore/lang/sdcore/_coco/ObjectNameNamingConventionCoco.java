/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdcore._coco;

import de.monticore.lang.sdcore._ast.ASTObject;
import de.monticore.lang.sdcore._cocos.SDCoreASTObjectCoCo;
import de.se_rwth.commons.logging.Log;

public class ObjectNameNamingConventionCoco implements SDCoreASTObjectCoCo {

  @Override
  public void check(ASTObject object) {
    if(object.isPresentName()) {
      String name = object.getName();
      if(name.isEmpty()) {
        Log.warn("Objectdeclaration introduces an object with name"
                + name
                + " which should either not be empty or not set");
      }
      if (Character.isUpperCase(name.charAt(0))) {
        Log.warn("Objectdeclaration introduces an object with name "
                + name
                + " which should be written lower case by convention"
        );
      }
    }
  }
}
