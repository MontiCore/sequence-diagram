/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd4code._coco;


import de.monticore.lang.sd4code._ast.ASTConstructorAction;
import de.monticore.lang.sdbase._ast.ASTOrdinaryInteraction;
import de.monticore.lang.sdbase._cocos.SDBaseASTOrdinaryInteractionCoCo;
import de.monticore.lang.sdcore._ast.ASTAction;
import de.monticore.lang.sdcore._ast.ASTInteractionEntity;
import de.monticore.lang.sdcore._ast.ASTObject;
import de.monticore.lang.sdcore._ast.ASTObjectInteractionEntity;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCObjectType;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.se_rwth.commons.logging.Log;

public class ConstructorActionRefersToCorrectTargetCoco implements SDBaseASTOrdinaryInteractionCoCo {
    static final String MESSAGE_ERROR_DOES_NOT_REFER_TO_OBJECT = "Constructor call does not refer to an object";
    static final String MESSAGE_ERROR_OBJECT_IS_NO_INLINE_DECLARATION = "Constructor call does not refer " +
            "to an inline declarated object";
    static final String MESSAGE_ERROR_CALL_REFERS_TO_WRONG_OBJECT = "The target type %s and the called constructor %s do not match.";

    private final MCBasicTypesPrettyPrinter prettyPrinter;
    public ConstructorActionRefersToCorrectTargetCoco() {
        this.prettyPrinter = new MCBasicTypesPrettyPrinter(new IndentPrinter());
    }

    @Override
    public void check(ASTOrdinaryInteraction interaction) {
        if(!interaction.isPresentTarget()) {
            return;
        }
        ASTInteractionEntity target = interaction.getTarget();
        ASTAction action = interaction.getAction();
        if(!(action instanceof ASTConstructorAction)) {
            return;
        }

        if(!(target instanceof ASTObjectInteractionEntity)) {
            Log.error(MESSAGE_ERROR_DOES_NOT_REFER_TO_OBJECT);
            return;
        }
        ASTObjectInteractionEntity objectInteractionEntity = (ASTObjectInteractionEntity) target;
        if(objectInteractionEntity.isPresentInlineDeclaration()) {
            Log.error(MESSAGE_ERROR_OBJECT_IS_NO_INLINE_DECLARATION);
            return;
        }
        ASTObject inlineDeclaratedObject = objectInteractionEntity.getInlineDeclaration();
        if(!inlineDeclaratedObject.isPresentMCObjectType()) {
            return;
        }
        ASTConstructorAction constructorAction = (ASTConstructorAction) action;
        ASTMCObjectType constructorObjectType = constructorAction.getMCObjectType();
        ASTMCObjectType inlineDeclaratedObjectType = constructorAction.getMCObjectType();
        if(!constructorObjectType.deepEquals(inlineDeclaratedObjectType)) {
            String constructorObjectTypeName = getNameOfMCObjectType(constructorObjectType);
            String inlineDeclaratedObjectTypeName = getNameOfMCObjectType(inlineDeclaratedObjectType);
            Log.error(String.format(MESSAGE_ERROR_CALL_REFERS_TO_WRONG_OBJECT,constructorObjectTypeName,inlineDeclaratedObjectTypeName));
        }
    }
    private String getNameOfMCObjectType(ASTMCObjectType astmcObjectType) {
        return astmcObjectType.printType(prettyPrinter);
    }


}
