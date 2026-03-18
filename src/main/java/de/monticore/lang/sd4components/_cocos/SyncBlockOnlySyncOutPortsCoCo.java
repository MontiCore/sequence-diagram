/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._cocos;

import de.monticore.lang.sd4components.SD4ComponentsMill;
import de.monticore.lang.sd4components._ast.ASTSDPort;
import de.monticore.lang.sd4components._ast.ASTSDSyncBlock;
import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.monticore.symbols.compsymbols._symboltable.PortSymbol;
import de.se_rwth.commons.logging.Log;
import java.util.stream.Collectors;

/**
 * CoCo: inside sync blocks outgoing ports must be time-synchronous.
 */
public class SyncBlockOnlySyncOutPortsCoCo implements SD4ComponentsASTSDSyncBlockCoCo {

  public static final String MESSAGE_ERROR = "0xB5009: "
    + "Non sync port in sync block";

  @Override
  public void check(ASTSDSyncBlock node) {
    //get messages
    for (ASTSDSendMessage message : node.streamSDElements()
      .filter(SD4ComponentsMill.typeDispatcher()::isSDBasisASTSDSendMessage)
      .map(SD4ComponentsMill.typeDispatcher()::asSDBasisASTSDSendMessage)
      .collect(Collectors.toList())) {
      if (message.isPresentSDSource() && SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDPort(message.getSDSource())
      ) {
        ASTSDPort sourcePort = SD4ComponentsMill.typeDispatcher().asSD4ComponentsASTSDPort(message.getSDSource());
        if (!sourcePort.isPresentPortSymbol()) {
          Log.warn("Skipping CoCo SyncBlockOnlySyncOutPortsCoCo");
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
