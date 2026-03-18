/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._cocos;

import de.monticore.lang.sd4components.SD4ComponentsMill;
import de.monticore.lang.sd4components._ast.ASTSDSyncBlock;
import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.se_rwth.commons.logging.Log;
import java.util.List;
import java.util.ArrayList;

/**
 * CoCo: inside sync blocks outgoing ports must send only once.
 */
public class SyncPortSendsOnceCoCo implements SD4ComponentsASTSDSyncBlockCoCo {

  public static final String MESSAGE_ERROR = "0xB500A: "
    + "Port sends multiple times in sync block";

  @Override
  public void check(ASTSDSyncBlock node) {
    List<String> traversed = new ArrayList<>();
    //get messages
    for (ASTSDSendMessage message : node.streamSDElements()
      .filter(SD4ComponentsMill.typeDispatcher()::isSDBasisASTSDSendMessage)
      .map(SD4ComponentsMill.typeDispatcher()::asSDBasisASTSDSendMessage)
      .toList()) {
      if (message.isPresentSDSource() && SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDPort(message.getSDSource())
      ) {
        String sourcePort = SD4ComponentsMill.typeDispatcher().asSD4ComponentsASTSDPort(message.getSDSource()).getName();
        if (!traversed.contains(sourcePort)){
            traversed.add(sourcePort);
          }else{
            Log.error(MESSAGE_ERROR, node.get_SourcePositionStart(), node.get_SourcePositionEnd());
          }
        }
      }
    }

}
