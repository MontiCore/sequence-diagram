/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdcore._coco;

import de.monticore.lang.sdcore._ast.ASTObject;
import de.monticore.lang.sdcore._cocos.SDCoreASTObjectCoCo;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCObjectType;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.se_rwth.commons.logging.Log;

public class ObjectTypeNamingConventionCoco implements SDCoreASTObjectCoCo {

  private final MCBasicTypesPrettyPrinter prettyPrinter;
  public ObjectTypeNamingConventionCoco() {
    this.prettyPrinter = new MCBasicTypesPrettyPrinter(new IndentPrinter());
  }

  @Override
  public void check(ASTObject object) {
    if(object.isPresentMCObjectType()) {
      ASTMCObjectType mcObjectType = object.getMCObjectType();
      String objectTypeName = mcObjectType.printType(prettyPrinter);
      if (Character.isLowerCase(objectTypeName.charAt(0))) {
        Log.warn("Objectdeclaration introduces an object with name "
                + objectTypeName
                + " which should be written upper case by convention.", object.get_SourcePositionStart()
        );
      }
    }
  }
}
