/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._cocos;

import de.monticore.lang.sd4components.SD4ComponentsMill;
import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSDSendMessageCoCo;
import de.monticore.symbols.compsymbols._symboltable.PortSymbol;
import de.se_rwth.commons.logging.Log;

/**
 * Checks that the timing of the source port of a connector matches the timing of the target port.
 */
public class MessageTimingFitCoCo implements SDBasisASTSDSendMessageCoCo {

  @Override
  public void check(ASTSDSendMessage node) {
    if (!node.isPresentSDTarget()
      || !SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDPort(node.getSDTarget())
      || !node.isPresentSDSource()
      || !SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDPort(node.getSDSource()))
      return;

    PortSymbol source = SD4ComponentsMill.typeDispatcher().asSD4ComponentsASTSDPort(node.getSDSource()).getPortSymbol();
    PortSymbol target = SD4ComponentsMill.typeDispatcher().asSD4ComponentsASTSDPort(node.getSDTarget()).getPortSymbol();

    if (!source.getTiming().matches(target.getTiming())) {
      Log.error("0xB5002: Connection timing mismatch", node.get_SourcePositionStart(), node.get_SourcePositionEnd());
    }
  }
}
