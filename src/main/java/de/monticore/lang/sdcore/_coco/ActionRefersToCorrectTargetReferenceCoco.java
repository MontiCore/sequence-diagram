/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdcore._coco;

import de.monticore.lang.sd._ast.ASTConstructorAction;
import de.monticore.lang.sd._ast.ASTMethodAction;
import de.monticore.lang.sdcore._ast.ASTInteraction;
import de.monticore.lang.sdcore._ast.ASTInteractionEntity;
import de.monticore.lang.sdcore._ast.ASTObjectInteractionEntity;
import de.monticore.lang.sdcore._cocos.SDCoreASTInteractionCoCo;
import de.se_rwth.commons.logging.Log;

public class ActionRefersToCorrectTargetReferenceCoco implements SDCoreASTInteractionCoCo {

  @Override
  public void check(ASTInteraction interaction) {
      ASTInteractionEntity target = interaction.getTarget();
      if(target instanceof ASTObjectInteractionEntity) {
          checkMethodCall(interaction, target);
      }
  }
  private void checkMethodCall(ASTInteraction interaction, ASTInteractionEntity targetReference) {
      if(interaction.getAction() instanceof ASTInteractionEntity) {
          ASTMethodAction action = (ASTMethodAction) interaction.getAction();
          if(targetReference == null) {
              Log.error("Target could not be identified since arrow type could not be identfied");
              return;
          }
          if(!action.isStatic()) {
              if(!checkReferenceOfStaticMethodCall(targetReference)) {
                  Log.error("Method call must refer to an class");
              }
          } else {
              if(!checkReferenceOfMethodCall(targetReference)) {
                  Log.error("Method call must refer to an object");
              }
          }
      }
  }
  //Todo: Compare function not implemented yet!
  private boolean compareTargetTypeAndConstructorCall(ASTConstructorAction constructorAction,ASTObjectReference targetReference) {
      return false;
  }
  private boolean isClassReference(ASTSDObject astsdObject) {
    return astsdObject.isPresentClass() && !astsdObject.isPresentClassOfObject();
  }
  private boolean isObjectInlineDeclaration(ASTSDObject astsdObject) {
    return astsdObject.isPresentName();
  }
  private boolean checkReferenceOfStaticMethodCall(ASTObjectReference reference) {
    if(reference.isPresentInlineDeclaration()) {
      return false;
    }
    if(reference.isPresentNameDefinition()) {
      return isClassReference(reference.getNameDefinition());
    }
    return true;
  }
  private boolean checkReferenceOfMethodCall(ASTObjectReference reference) {
    if(reference.isPresentNameDefinition()) {
      return isClassReference(reference.getNameDefinition());
    }
    return !isObjectInlineDeclaration(reference.getNameDefinition());
  }
}
