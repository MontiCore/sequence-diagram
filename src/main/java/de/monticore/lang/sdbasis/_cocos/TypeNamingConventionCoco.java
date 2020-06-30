/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedType;
import de.monticore.types.mcbasictypes._cocos.MCBasicTypesASTMCQualifiedTypeCoCo;
import de.se_rwth.commons.logging.Log;

public class TypeNamingConventionCoco implements MCBasicTypesASTMCQualifiedTypeCoCo {

  static final String MESSAGE_WARNING_UPPER_CASE = TypeNamingConventionCoco.class.getSimpleName() + ": "
          + "Object declaration introduces an object type name "
          + "which should be written upper case by convention.";

  @Override
  public void check(ASTMCQualifiedType mcObjectType) {
      String objectTypeName = mcObjectType.getNameList().get(mcObjectType.getNameList().size() - 1);
      if (Character.isLowerCase(objectTypeName.charAt(0))) {
        Log.warn(String.format(MESSAGE_WARNING_UPPER_CASE, objectTypeName), mcObjectType.get_SourcePositionStart());
      }
  }
}

