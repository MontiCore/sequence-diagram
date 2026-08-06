/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._cocos;

import de.monticore.lang.sd4components._ast.ASTSDPort;
import de.monticore.lang.sd4components._ast.ASTSDTick;
import de.monticore.lang.sdbasis._ast.ASTSDBody;
import de.monticore.lang.sdbasis._ast.ASTSDElement;
import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.monticore.lang.sdbasis._ast.ASTSequenceDiagram;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSequenceDiagramCoCo;
import de.monticore.symbols.compsymbols._symboltable.Timing;
import de.se_rwth.commons.logging.Log;

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
        if (!(node.getSDElement(i) instanceof ASTSDTick)
            && checkIfSyncMessage(node.getSDElement(i))) {
          Log.error(MESSAGE_ERROR, node.getSDElement(i).get_SourcePositionStart(),
              node.getSDElement(i).get_SourcePositionEnd());
        }
      }
    }
  }

  public boolean checkIfSyncMessage(ASTSDElement element) {
    if (element instanceof ASTSDSendMessage m) {
      if (m.isPresentSDSource() && m.getSDSource() instanceof ASTSDPort source) {
        if (source.isPresentPortSymbol()) {
          return source.getPortSymbol().getTiming().matches(Timing.TIMED_SYNC);
        }
      }
    }
    return false;
  }
}
