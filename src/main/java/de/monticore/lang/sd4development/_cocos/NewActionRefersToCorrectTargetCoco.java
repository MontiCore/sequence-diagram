/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd4development._cocos;

import de.monticore.lang.sd4development._ast.ASTSDNew;
import de.monticore.lang.sdbasis._ast.ASTSDObjectTarget;
import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSDSendMessageCoCo;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.se_rwth.commons.logging.Log;

public class NewActionRefersToCorrectTargetCoco implements SDBasisASTSDSendMessageCoCo {

  static final String MESSAGE_ERROR_DOES_NOT_REFER_TO_OBJECT = NewActionRefersToCorrectTargetCoco.class.getSimpleName() + ": " +
          "Constructor call does not refer to an object";

  static final String MESSAGE_ERROR_CALL_REFERS_TO_WRONG_OBJECT = NewActionRefersToCorrectTargetCoco.class.getSimpleName() + ": " +
          "The target type %s and the called constructor %s do not match.";

  private final MCBasicTypesPrettyPrinter prettyPrinter;

  public NewActionRefersToCorrectTargetCoco() {
    this.prettyPrinter = new MCBasicTypesPrettyPrinter(new IndentPrinter());
  }

  @Override
  public void check(ASTSDSendMessage node) {
    if (!node.isPresentSDTarget()) {
      return;
    }
    if (!(node.getSDAction() instanceof ASTSDNew)) {
      return;
    }
    if (!(node.getSDTarget() instanceof ASTSDObjectTarget)) {
      Log.error(MESSAGE_ERROR_DOES_NOT_REFER_TO_OBJECT, node.get_SourcePositionStart());
      return;
    }
    ASTSDObjectTarget objectTarget = (ASTSDObjectTarget) node.getSDTarget();
    ASTSDNew newAction = (ASTSDNew) node.getSDAction();
    String newActionMCObjectType = newAction.getMCObjectType().printType(prettyPrinter);
    String variableType = objectTarget.getNameSymbol().getType().print();

    if (!newActionMCObjectType.equals(variableType)) {
      Log.error(String.format(MESSAGE_ERROR_CALL_REFERS_TO_WRONG_OBJECT, newActionMCObjectType, variableType));
    }
  }
}
