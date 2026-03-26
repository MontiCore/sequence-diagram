/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._cocos;

import de.monticore.lang.sd4components.SD4ComponentsMill;
import de.monticore.lang.sd4components._ast.ASTSDTick;
import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.se_rwth.commons.logging.Log;
import java.util.List;
import java.util.ArrayList;

/**
 * CoCo: inside tick blocks incoming ports must receive only once.
 */
public class SyncPortsReceiveOnlyOnceCoCo implements SD4ComponentsASTSDTickCoCo {

  public static final String MESSAGE_ERROR = "0xB500A: "
    + "Port receives multiple times in tick block";

  @Override
  public void check(ASTSDTick node) {
    List<String> traversed = new ArrayList<>();
    for (ASTSDSendMessage message : node.streamSDSendMessages().toList()) {
      if (message.isPresentSDTarget()
          && SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDPort(message.getSDTarget())) {
        String targetPort = SD4ComponentsMill.typeDispatcher()
            .asSD4ComponentsASTSDPort(message.getSDTarget()).getName()
            + "." + SD4ComponentsMill.typeDispatcher()
            .asSD4ComponentsASTSDPort(message.getSDTarget()).getPort();
        if (!traversed.contains(targetPort)) {
          traversed.add(targetPort);
        } else {
          Log.error(MESSAGE_ERROR, node.get_SourcePositionStart(), node.get_SourcePositionEnd());
        }
      }
    }
  }
}
