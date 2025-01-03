package de.monticore.lang.sd4components._cocos;

import de.monticore.lang.sd4components.SD4ComponentsMill;
import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSDSendMessageCoCo;
import de.se_rwth.commons.logging.Log;

/**
 * Trigger messages require a target.
 */
public class TriggerMessageRequiresTargetCoCo implements SDBasisASTSDSendMessageCoCo {

  public static final String MESSAGE_ERROR = "0xB5008: "
    + "The interaction is a trigger but has no target";

  @Override
  public void check(ASTSDSendMessage node) {
    if (!node.isPresentSDTarget()
      && SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDMessage(node.getSDAction())
      && SD4ComponentsMill.typeDispatcher().asSD4ComponentsASTSDMessage(node.getSDAction()).isTrigger()) {
      Log.error(String.format(MESSAGE_ERROR), node.get_SourcePositionStart(), node.get_SourcePositionEnd());
    }
  }
}
