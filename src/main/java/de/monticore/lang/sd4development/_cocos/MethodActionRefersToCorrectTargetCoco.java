/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd4development._cocos;

import de.monticore.lang.sd4development._ast.ASTSDCall;
import de.monticore.lang.sd4development._ast.ASTSDClass;
import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSDSendMessageCoCo;
import de.se_rwth.commons.logging.Log;

/**
 * Checks if a method action refers to a correct target, i.e.,
 * that static methods are only invoked on classes,
 * and usual invocations on objects
 */
public class MethodActionRefersToCorrectTargetCoco implements SDBasisASTSDSendMessageCoCo {

  private static final String MESSAGE_ERROR_REFERS_TO_ = "0xS0011: "
          + "Method call must refer to an %s";

  @Override
  public void check(ASTSDSendMessage node) {
    if (!node.isPresentSDTarget()) {
      return;
    }
    if (!(node.getSDAction() instanceof ASTSDCall)) {
      return;
    }
    ASTSDCall sdCall = (ASTSDCall) node.getSDAction();
    if (sdCall.isStatic() != (node.getSDTarget() instanceof ASTSDClass)) {
      String refersTo = sdCall.isStatic() ? "object" : "class";
      Log.error(String.format(MESSAGE_ERROR_REFERS_TO_, refersTo), node.get_SourcePositionStart());
    }
  }
}
