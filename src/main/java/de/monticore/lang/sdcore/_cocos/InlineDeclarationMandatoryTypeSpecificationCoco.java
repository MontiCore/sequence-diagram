/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdcore._coco;

import de.monticore.lang.sdcore._ast.ASTObject;
import de.monticore.lang.sdcore._ast.ASTObjectInteractionEntity;
import de.monticore.lang.sdcore._cocos.SDCoreASTObjectCoCo;
import de.monticore.lang.sdcore._cocos.SDCoreASTObjectInteractionEntityCoCo;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.se_rwth.commons.logging.Log;

public class InlineDeclarationMandatoryTypeSpecificationCoco implements SDCoreASTObjectInteractionEntityCoCo {
  static final String MESSAGE_ERROR_MANDATORY_TYPE_SPECIFICATION = "The type must be specified";

  private final MCBasicTypesPrettyPrinter prettyPrinter;
  public InlineDeclarationMandatoryTypeSpecificationCoco() {
    this.prettyPrinter = new MCBasicTypesPrettyPrinter(new IndentPrinter());
  }
  @Override
  public void check(ASTObjectInteractionEntity object) {
    if(!(object.isPresentInlineDeclaration() && object.getInlineDeclaration().isPresentMCObjectType())) {
      Log.error(MESSAGE_ERROR_MANDATORY_TYPE_SPECIFICATION);
    }
  }
}
