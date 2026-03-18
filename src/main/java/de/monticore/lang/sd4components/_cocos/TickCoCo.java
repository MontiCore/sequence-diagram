package de.monticore.lang.sd4components._cocos;

import de.monticore.lang.sd4components._ast.ASTSDPort;
import de.monticore.lang.sdbasis._ast.ASTSDBody;
import de.monticore.lang.sdbasis._ast.ASTSDElement;
import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.monticore.lang.sdbasis._ast.ASTSequenceDiagram;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSequenceDiagramCoCo;
import de.monticore.umlstereotype._ast.ASTStereoValueTOP;
import de.se_rwth.commons.logging.Log;
import de.monticore.lang.sd4components.SD4ComponentsMill;
import java.util.HashMap;

/**
 * When complete, two synchronous messages within the same tick lifetime cannot be sent from the same port.
 */
public class TickCoCo implements SDBasisASTSequenceDiagramCoCo {

  public static final String MESSAGE_ERROR = "0xB500C: "
    + "Two sync ports send within a tick lifetime.";

  @Override
  public void check(ASTSequenceDiagram sd) {
    if (!sd.isPresentStereotype()) return;
    if (sd.getStereotype().getValuesList().stream().map(ASTStereoValueTOP::getName).toList().contains("complete")) {
      ASTSDBody node = sd.getSDBody();
      HashMap<String, ASTSDSendMessage> messages = new HashMap<>();

      for (int i = 0; i < node.getSDElementList().size() - 1; i++) {
        if (SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDTick(node.getSDElement(i))) {
          messages.clear();
        } else {
          if (!messages.isEmpty() && checkIfSyncMessage(node.getSDElement(i))) {
            ASTSDSendMessage m = SD4ComponentsMill.typeDispatcher().asSDBasisASTSDSendMessage(node.getSDElement(i));
            ASTSDPort source = SD4ComponentsMill.typeDispatcher().asSD4ComponentsASTSDPort(m.getSDSource());
            if (messages.containsKey(source.getName())) {
              Log.error(MESSAGE_ERROR, node.get_SourcePositionStart(), node.get_SourcePositionEnd());
            } else {
              messages.put(source.getName(), m);
            }
          } else if (messages.isEmpty() && checkIfSyncMessage(node.getSDElement(i))) {
            ASTSDSendMessage m = SD4ComponentsMill.typeDispatcher().asSDBasisASTSDSendMessage(node.getSDElement(i));
            messages.put(SD4ComponentsMill.typeDispatcher().asSD4ComponentsASTSDPort(m.getSDSource()).getName(), m);
          }
        }

      }
    }
  }

    public boolean checkIfSyncMessage(ASTSDElement element){
      if(SD4ComponentsMill.typeDispatcher().isSDBasisASTSDSendMessage(element)){
        ASTSDSendMessage m = SD4ComponentsMill.typeDispatcher().asSDBasisASTSDSendMessage(element);
        if(m.isPresentSDSource() && SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDPort(m.getSDSource())){
          ASTSDPort source = SD4ComponentsMill.typeDispatcher().asSD4ComponentsASTSDPort(m.getSDSource());
          return source.getPortSymbol().getTiming().getName().equals("sync");
        }
      }
      return false;

  }
}
