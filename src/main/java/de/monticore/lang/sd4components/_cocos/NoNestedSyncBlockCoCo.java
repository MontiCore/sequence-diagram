/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._cocos;

import de.monticore.lang.sd4components.SD4ComponentsMill;
import de.monticore.lang.sd4components._ast.ASTSDSyncBlock;
import de.se_rwth.commons.logging.Log;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CoCo: Sync blocks may not contain other sync blocks.
 */
public class NoNestedSyncBlockCoCo implements SD4ComponentsASTSDSyncBlockCoCo {

  public static final String MESSAGE_ERROR = "0xB500D: "
    + "Nested sync block not allowed";

  @Override
  public void check(ASTSDSyncBlock node) {
    //get nested blocks
    List blocks = node.streamSDElements()
      .filter(SD4ComponentsMill.typeDispatcher()::isSD4ComponentsASTSDSyncBlock)
      .map(SD4ComponentsMill.typeDispatcher()::asSD4ComponentsASTSDSyncBlock)
      .collect(Collectors.toList());
    if (!blocks.isEmpty()) {
      Log.error(MESSAGE_ERROR, node.get_SourcePositionStart(), node.get_SourcePositionEnd());
    }
  }
}
