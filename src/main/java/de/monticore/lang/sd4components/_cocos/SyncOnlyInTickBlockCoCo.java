/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._cocos;

import de.monticore.lang.sd4components._ast.ASTSDPort;
import de.monticore.lang.sdbasis._ast.ASTSDBody;
import de.monticore.lang.sdbasis._ast.ASTSDElement;
import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.monticore.lang.sdbasis._ast.ASTSequenceDiagram;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSequenceDiagramCoCo;
import de.se_rwth.commons.logging.Log;
import de.monticore.lang.sd4components.SD4ComponentsMill;

/**
 * Synchronous messages should only be declared within a tick block.
 */
public class SyncOnlyInTickBlockCoCo implements SDBasisASTSequenceDiagramCoCo {

  public static final String MESSAGE_ERROR = "0xB500C: "
    + "Sync message outside of tick block";

  @Override
  public void check(ASTSequenceDiagram sd) {
    ASTSDBody node = sd.getSDBody();

    if (!node.getSDElementList().isEmpty()) {
      for (int i = 0; i < node.getSDElementList().size(); i++) {
        if (!SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDTick(node.getSDElement(i))
            && checkIfSyncMessage(node.getSDElement(i))) {
          Log.error(MESSAGE_ERROR,
              node.getSDElement(i).get_SourcePositionStart(),
              node.getSDElement(i).get_SourcePositionEnd());
        }
      }
    }
  }

  public boolean checkIfSyncMessage(ASTSDElement element) {
    if (SD4ComponentsMill.typeDispatcher().isSDBasisASTSDSendMessage(element)) {
      ASTSDSendMessage m = SD4ComponentsMill.typeDispatcher().asSDBasisASTSDSendMessage(element);
      if (m.isPresentSDSource()
          && SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDPort(m.getSDSource())) {
        ASTSDPort source = SD4ComponentsMill.typeDispatcher().asSD4ComponentsASTSDPort(m.getSDSource());
        if (source.isPresentPortSymbol()) {
          return source.getPortSymbol().getTiming().getName().equals("sync");
        }
      }
    }
    return false;
  }
}
