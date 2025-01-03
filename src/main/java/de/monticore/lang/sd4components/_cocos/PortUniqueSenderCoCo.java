/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._cocos;

import de.monticore.lang.sd4components.SD4ComponentsMill;
import de.monticore.lang.sd4components._ast.ASTSDPort;
import de.monticore.lang.sdbasis._ast.ASTSDBody;
import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.monticore.lang.sdbasis._cocos.SDBasisASTSDBodyCoCo;
import de.se_rwth.commons.logging.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
    Map<String, String> targetSource = new HashMap<>();
    for (ASTSDSendMessage connector : node.streamSDElements()
      .filter(SD4ComponentsMill.typeDispatcher()::isSDBasisASTSDSendMessage)
      .map(SD4ComponentsMill.typeDispatcher()::asSDBasisASTSDSendMessage)
      .collect(Collectors.toList())) {
      if (connector.isPresentSDTarget()
        && SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDPort(connector.getSDTarget())) {
        String source = "";
        if (connector.isPresentSDSource()
          && SD4ComponentsMill.typeDispatcher().isSD4ComponentsASTSDPort(connector.getSDSource())) {
          ASTSDPort astSource = SD4ComponentsMill.typeDispatcher().asSD4ComponentsASTSDPort(connector.getSDSource());
          source = astSource.getName() + "." + astSource.getPort();
        }
        ASTSDPort astTarget = SD4ComponentsMill.typeDispatcher().asSD4ComponentsASTSDPort(connector.getSDTarget());
        String target = astTarget.getName() + "." + astTarget.getPort();
        if (targetSource.containsKey(target) && !targetSource.get(target).equals(source)) {
          Log.error(String.format(MESSAGE_ERROR, target), connector.get_SourcePositionStart(), connector.get_SourcePositionEnd());
        } else if (!source.isEmpty()) {
          targetSource.put(target, source);
        }
      }
    }
  }
}
