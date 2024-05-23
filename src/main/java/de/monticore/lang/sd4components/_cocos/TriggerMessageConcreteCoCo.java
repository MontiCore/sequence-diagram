/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._cocos;

import de.monticore.lang.sd4components.SD4ComponentsMill;
import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSDSendMessageCoCo;
import de.se_rwth.commons.logging.Log;

/**
 * Checks that a triggering message has a concrete value and is not incomplete
 */
public class TriggerMessageConcreteCoCo implements SDBasisASTSDSendMessageCoCo {

  public static final String MESSAGE_ERROR = "0xB5006: "
    + "The interaction has a triggering action but no concrete value";

  @Override
  public void check(ASTSDSendMessage object) {
    if (!object.isPresentSDSource() && SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDIncompleteAction(object.getSDAction())) {
      Log.error(MESSAGE_ERROR, object.get_SourcePositionStart(), object.get_SourcePositionEnd());
    }
  }
}
