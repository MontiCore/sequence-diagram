/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._cocos;

import de.monticore.lang.sd4components.SD4ComponentsMill;
import de.monticore.lang.sd4components._ast.ASTSDSyncBlock;
import de.se_rwth.commons.logging.Log;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CoCo: inside sync blocks there should be no ticks.
 */
public class SyncBlockNoTicksCoCo implements SD4ComponentsASTSDSyncBlockCoCo {

  public static final String MESSAGE_ERROR = "0xB500B: "
    + "Sync block contains tick";

  @Override
  public void check(ASTSDSyncBlock node) {
    //get messages
    List ticks = node.streamSDElements()
      .filter(SD4ComponentsMill.typeDispatcher()::isSD4ComponentsASTSDTick)
      .map(SD4ComponentsMill.typeDispatcher()::asSD4ComponentsASTSDTick)
      .collect(Collectors.toList());
      if (!ticks.isEmpty()) {
        Log.error(MESSAGE_ERROR, node.get_SourcePositionStart(), node.get_SourcePositionEnd());
      }
  }
}
