/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd4code._coco;


import de.monticore.lang.sd4code._ast.ASTClassInteractionEntity;
import de.monticore.lang.sd4code._ast.ASTMethodInvocationAction;
import de.monticore.lang.sdbase._ast.ASTOrdinaryInteraction;
import de.monticore.lang.sdbase._cocos.SDBaseASTOrdinaryInteractionCoCo;
import de.monticore.lang.sdcore._ast.ASTAction;
import de.monticore.lang.sdcore._ast.ASTInteractionEntity;
import de.monticore.lang.sdcore._ast.ASTObjectInteractionEntity;
import de.se_rwth.commons.logging.Log;

public class MethodActionRefersToCorrectTargetCoco implements SDBaseASTOrdinaryInteractionCoCo {

    final static String MESSAGE_ERROR_REFERS_TO_OBJECT = "Method call must refer to an class";
    final static String MESSAGE_ERROR_REFERS_TO_CLASS = "Method call must refer to an object";
    @Override
    public void check(ASTOrdinaryInteraction interaction) {
        if(!interaction.isPresentTarget()) {
            return;
        }
        ASTInteractionEntity target = interaction.getTarget();
        ASTAction action = interaction.getAction();
        if(!(action instanceof ASTMethodInvocationAction)) {
            return;
        }
        ASTMethodInvocationAction methodInvocationAction = (ASTMethodInvocationAction) action;
        if(methodInvocationAction.isStatic() && target instanceof ASTObjectInteractionEntity) {
            Log.error(MESSAGE_ERROR_REFERS_TO_OBJECT);
        }
        else if((!methodInvocationAction.isStatic()) && target instanceof ASTClassInteractionEntity) {
            Log.error(MESSAGE_ERROR_REFERS_TO_CLASS);
        }
    }
}
