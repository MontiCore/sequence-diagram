/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedType;
import de.monticore.types.mcbasictypes._cocos.MCBasicTypesASTMCQualifiedTypeCoCo;
import de.se_rwth.commons.logging.Log;

/**
 * Checks if the naming convention for types is satisfied, i.e. that type names
 * start with upper case letters.
 */
public class TypeNamingConventionCoco implements MCBasicTypesASTMCQualifiedTypeCoCo {

  private static final String MESSAGE_WARNING_UPPER_CASE = "0xB0023: "
          + "Object declaration introduces the object type %s "
          + "which should be written upper case by convention.";

  @Override
  public void check(ASTMCQualifiedType mcObjectType) {
      String objectTypeName = mcObjectType.getNameList().get(mcObjectType.getNameList().size() - 1);
      if (!Character.isUpperCase(objectTypeName.charAt(0))) {
        Log.warn(String.format(MESSAGE_WARNING_UPPER_CASE, objectTypeName), mcObjectType.get_SourcePositionStart());
      }
  }
}

