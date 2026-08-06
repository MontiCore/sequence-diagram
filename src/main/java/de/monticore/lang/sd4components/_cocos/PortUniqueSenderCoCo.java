/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._cocos;

import de.monticore.lang.sd4components._ast.ASTSDPort;
import de.monticore.lang.sd4components._ast.ASTSDTick;
import de.monticore.lang.sdbasis._ast.ASTSDBody;
import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSDBodyCoCo;
import de.se_rwth.commons.logging.Log;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Implements [Hab16] R1: Each outgoing port of a component type definition is
 * used at most once as target of a connector. (p. 63, Lst. 3.36)
 * Implements [Hab16] R2: Each incoming port of a subcomponent is used at most
 * once as target of a connector. (p. 62, Lst. 3.37)
 */
public class PortUniqueSenderCoCo implements SDBasisASTSDBodyCoCo {

  public static final String MESSAGE_ERROR = "0xB5003: "
    + "Port '%s' is target of multiple connectors";
  
  @Override
  public void check(ASTSDBody node) {
    Map<String, String> targetSource = new LinkedHashMap<>();
    List<ASTSDSendMessage> connectors = node.streamSDElements().flatMap(e -> {
      if (e instanceof ASTSDSendMessage astsdSendMessage) {
        return Stream.of(astsdSendMessage);
      }
      if (e instanceof ASTSDTick astsdTick) {
        return astsdTick.streamSDSendMessages();
      }
      return Stream.empty();
    }).toList();
    
    for (ASTSDSendMessage connector : connectors) {
      if (connector.isPresentSDTarget() &&
          connector.getSDTarget() instanceof ASTSDPort targetPort) {
        String source = "";
        if (connector.isPresentSDSource() &&
            connector.getSDSource() instanceof ASTSDPort sourcePort) {
          source = sourcePort.getName() + "." + sourcePort.getPort();
        }
        String target = targetPort.getName() + "." + targetPort.getPort();
        if (targetSource.containsKey(target) && !targetSource.get(target).equals(source)) {
          Log.error(String.format(MESSAGE_ERROR, target), connector.get_SourcePositionStart(),
              connector.get_SourcePositionEnd());
        }
        else if (!source.isEmpty()) {
          targetSource.put(target, source);
        }
      }
    }
  }
}
