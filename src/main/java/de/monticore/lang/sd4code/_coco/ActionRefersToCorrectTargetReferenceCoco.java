/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd._cocos;

import de.monticore.lang.sd._ast.ASTBasicArrow;
import de.monticore.lang.sd._ast.ASTConstructorAction;
import de.monticore.lang.sd._ast.ASTDashedArrow;
import de.monticore.lang.sd._ast.ASTMethodAction;
import de.monticore.lang.sdbase._ast.*;
import de.monticore.lang.sdbase._cocos.SDBaseASTInteractionCoCo;
import de.monticore.types.mcbasictypes._ast.ASTMCQualifiedName;
import de.se_rwth.commons.logging.Log;

public class ActionRefersToCorrectTargetReferenceCoco implements SDBaseASTInteractionCoCo {
    static final string
    @Override
    public void check(ASTInteraction interaction) {
        ASTObjectReference targetReference = targetIdentification(interaction);
        if(targetReference == null) {
            Log.error("Target could not be identified since arrow type could not be identfied");
            return;
        }
        checkMethodCall(interaction, targetReference);
        checkConstructorCall(interaction, targetReference);
    }
    private void checkConstructorCall(ASTInteraction interaction, ASTObjectReference targetReference) {
        if(interaction.getAction() instanceof ASTConstructorAction) {
            ASTConstructorAction action = (ASTConstructorAction) interaction.getAction();
            if(!targetReference.isPresentInlineDeclaration()) {
                Log.error("The call of constructor ... does not define a new object of type ... as target object.");
            }
            if(!compareTargetTypeAndConstructorCall(action, targetReference)) {
                Log.error("The target type ... and the called constructor ... do not match.");
            }

        }
    }
    private void checkMethodCall(ASTInteraction interaction, ASTObjectReference targetReference) {
        if(interaction.getAction() instanceof ASTMethodAction) {
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
    private ASTObjectReference targetIdentification(ASTInteraction interaction) {
        ASTArrow arrow = interaction.getArrow();
        //identify target if BasicArrow
        if(arrow instanceof ASTBasicArrow) {
            if(((ASTBasicArrow) arrow).isToRight()) {
                return interaction.getRight();
            }
            else {
                return interaction.getLeft();
            }
        }
        //identify target if dashedArrow
        if(arrow instanceof ASTDashedArrow) {
            if(((ASTDashedArrow) arrow).isToRight()) {
                return interaction.getRight();
            } else {
                return interaction.getLeft();
            }
        }
        return null;
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
