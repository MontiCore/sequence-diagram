/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._cocos;

import de.monticore.lang.sd4components._ast.ASTSDMessage;
import de.monticore.lang.sd4components._ast.ASTSDPort;
import de.monticore.lang.sd4components._ast.ASTSDTick;
import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.monticore.lang.sdbasis._ast.ASTSequenceDiagram;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSequenceDiagramCoCo;
import de.se_rwth.commons.logging.Log;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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
        if (e instanceof ASTSDSendMessage message)
          return Stream.of(message);
        if (e instanceof ASTSDTick tick)
          return tick.streamSDSendMessages();
        return Stream.empty();
      })
      .filter(e -> e.getSDAction() instanceof ASTSDMessage message && message.isTrigger())
      .filter(ASTSDSendMessage::isPresentSDTarget)
      .map(ASTSDSendMessage::getSDTarget)
      .filter(e -> e instanceof ASTSDPort)
      .map(e -> (ASTSDPort) e)
      .toList();

    Set<String> connectedPorts = getConnectedPorts(node);

    for (ASTSDPort target : targets) {
      if (connectedPorts.contains(target.getName() + "." + target.getPort())) {
        Log.error(String.format(MESSAGE_ERROR, target.getName() + "." + target.getPort()),
            target.get_SourcePositionStart(), target.get_SourcePositionEnd());
      }
    }
  }

  protected Set<String> getConnectedPorts(ASTSequenceDiagram node) {
    Set<String> targets = new LinkedHashSet<>();
    List<ASTSDSendMessage> connectors = node.getSDBody().streamSDElements().flatMap(e -> {
      if (e instanceof ASTSDSendMessage message) {
        return Stream.of(message);
      }
      if (e instanceof ASTSDTick tick) {
        return tick.streamSDSendMessages();
      }
      return Stream.empty();
    }).toList();

    for (ASTSDSendMessage connector : connectors) {
      if (connector.isPresentSDTarget()
        && connector.getSDTarget() instanceof ASTSDPort targetPort
        && connector.isPresentSDSource()
        && connector.getSDSource() instanceof ASTSDPort) {
        targets.add(targetPort.getName() + "." + targetPort.getPort());
      }
    }
    return targets;
  }
}
