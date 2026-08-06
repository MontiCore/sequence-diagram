/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._cocos;

import de.monticore.lang.sd4components._ast.ASTSDPort;
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
      if (message.isPresentSDTarget() && message.getSDTarget() instanceof ASTSDPort targetPort) {
        String targetPortName = targetPort.getName() + "." + targetPort.getPort();
        if (!traversed.contains(targetPortName)) {
          traversed.add(targetPortName);
        }
        else {
          Log.error(MESSAGE_ERROR, node.get_SourcePositionStart(), node.get_SourcePositionEnd());
        }
      }
    }
  }
}
