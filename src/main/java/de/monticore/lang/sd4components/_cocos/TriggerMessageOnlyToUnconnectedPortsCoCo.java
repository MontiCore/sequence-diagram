/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._cocos;

import de.monticore.lang.sd4components.SD4ComponentsMill;
import de.monticore.lang.sd4components._ast.ASTSDPort;
import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.monticore.lang.sdbasis._ast.ASTSequenceDiagram;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSequenceDiagramCoCo;
import de.se_rwth.commons.logging.Log;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Trigger messages can only be sent to ports that are not connected to other ports.
 */
public class TriggerMessageOnlyToUnconnectedPortsCoCo implements SDBasisASTSequenceDiagramCoCo {

  public static final String MESSAGE_ERROR = "0xB5006: "
    + "The port '%s' is already connected and cannot be used as a trigger";

  @Override
  public void check(ASTSequenceDiagram node) {
    List<ASTSDPort> targets = node.getSDBody().streamSDElements()
      .flatMap(e -> {
        if (SD4ComponentsMill.typeDispatcher().isSDBasisASTSDSendMessage(e))
          return Stream.of(SD4ComponentsMill.typeDispatcher().asSDBasisASTSDSendMessage(e));
        if (SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDTick(e))
          return SD4ComponentsMill.typeDispatcher().asSD4ComponentsASTSDTick(e).streamSDSendMessages();
        return Stream.empty();
      })
      .filter(e -> SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDMessage(e.getSDAction()))
      .filter(e -> SD4ComponentsMill.typeDispatcher().asSD4ComponentsASTSDMessage(e.getSDAction()).isTrigger())
      .filter(ASTSDSendMessage::isPresentSDTarget)
      .map(ASTSDSendMessage::getSDTarget)
      .filter(e -> SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDPort(e))
      .map(e -> SD4ComponentsMill.typeDispatcher().asSD4ComponentsASTSDPort(e))
      .collect(Collectors.toList());

    Set<String> connectedPorts = getConnectedPorts(node);

    for (ASTSDPort target : targets) {
      if (connectedPorts.contains(target.getName() + "." + target.getPort())) {
        Log.error(String.format(MESSAGE_ERROR, target.getName() + "." + target.getPort()), target.get_SourcePositionStart(), target.get_SourcePositionEnd());
      }
    }
  }

  protected Set<String> getConnectedPorts(ASTSequenceDiagram node) {
    Set<String> targets = new LinkedHashSet<>();
    for (ASTSDSendMessage connector : node.getSDBody().streamSDElements()
      .flatMap(e -> {
        if (SD4ComponentsMill.typeDispatcher().isSDBasisASTSDSendMessage(e))
          return Stream.of(SD4ComponentsMill.typeDispatcher().asSDBasisASTSDSendMessage(e));
        if (SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDTick(e))
          return SD4ComponentsMill.typeDispatcher().asSD4ComponentsASTSDTick(e).streamSDSendMessages();
        return Stream.empty();
      })
      .collect(Collectors.toList())) {
      if (connector.isPresentSDTarget()
        && SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDPort(connector.getSDTarget())
        && connector.isPresentSDSource()
        && SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDPort(connector.getSDSource())) {
        ASTSDPort astTarget = SD4ComponentsMill.typeDispatcher().asSD4ComponentsASTSDPort(connector.getSDTarget());
        targets.add(astTarget.getName() + "." + astTarget.getPort());
      }
    }
    return targets;
  }
}
