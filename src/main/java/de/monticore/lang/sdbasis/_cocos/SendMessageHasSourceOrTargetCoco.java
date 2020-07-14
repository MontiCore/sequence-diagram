/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.se_rwth.commons.logging.Log;

/**
 * Checks if a SDSendMessage as at least a source or a target
 */
public class SendMessageHasSourceOrTargetCoco implements SDBasisASTSDSendMessageCoCo {

  private static final String MESSAGE_ERROR = "0xB0022: "
          + "The interaction has neither a source nor a target. At least one of them must be set.";

  @Override
  public void check(ASTSDSendMessage object) {
    if(!object.isPresentSDTarget() && !object.isPresentSDSource()) {
      Log.error(MESSAGE_ERROR);
    }
  }
}
