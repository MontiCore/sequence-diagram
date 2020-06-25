/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.sdbasis._ast.ASTSDObject;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCObjectType;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.se_rwth.commons.logging.Log;

@Deprecated
public class ObjectTypeNamingConventionCoco implements SDBasisASTSDObjectCoCo {

  static final String MESSAGE_WARNING_UPPER_CASE = ObjectTypeNamingConventionCoco.class.getSimpleName() + ": "
          + "Object declaration introduces an object with name %s "
          + "which should be written upper case by convention.";

  private final MCBasicTypesPrettyPrinter prettyPrinter;

  public ObjectTypeNamingConventionCoco() {
    this.prettyPrinter = new MCBasicTypesPrettyPrinter(new IndentPrinter());
  }

  @Override
  public void check(ASTSDObject object) {
    if(object.isPresentMCObjectType()) {
      ASTMCObjectType mcObjectType = object.getMCObjectType();
      String objectTypeName = mcObjectType.printType(prettyPrinter);
      if (Character.isLowerCase(objectTypeName.charAt(0))) {
        Log.warn(String.format(MESSAGE_WARNING_UPPER_CASE, objectTypeName), object.get_SourcePositionStart()
        );
      }
    }
  }
}
