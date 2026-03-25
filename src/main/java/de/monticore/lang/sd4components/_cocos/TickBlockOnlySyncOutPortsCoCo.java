/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._cocos;

import de.monticore.lang.sd4components.SD4ComponentsMill;
import de.monticore.lang.sd4components._ast.ASTSDPort;
import de.monticore.lang.sd4components._ast.ASTSDTick;
import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.monticore.symbols.compsymbols._symboltable.PortSymbol;
import de.se_rwth.commons.logging.Log;
import java.util.stream.Collectors;

/**
 * CoCo: inside tick blocks outgoing ports must be time-synchronous.
 */
public class TickBlockOnlySyncOutPortsCoCo implements SD4ComponentsASTSDTickCoCo {

  public static final String MESSAGE_ERROR = "0xB5009: "
    + "Non sync port in tick block";

  @Override
  public void check(ASTSDTick node) {
    //get messages
    for (ASTSDSendMessage message : node.streamSDSendMessages().toList()) {
      if (message.isPresentSDSource() && SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDPort(message.getSDSource())
      ) {
        ASTSDPort sourcePort = SD4ComponentsMill.typeDispatcher().asSD4ComponentsASTSDPort(message.getSDSource());
        if (!sourcePort.isPresentPortSymbol()) {
          Log.warn("Skipping CoCo TickBlockOnlySyncOutPortsCoCo");
          return;
        } else {
          PortSymbol source = sourcePort.getPortSymbol();
          if (!source.getTiming().getName().matches("sync")) {
            Log.error(MESSAGE_ERROR, node.get_SourcePositionStart(), node.get_SourcePositionEnd());
          }
        }
      }
    }
  }
}
