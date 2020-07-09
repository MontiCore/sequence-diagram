/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;


import de.monticore.lang.sdbasis._ast.*;
import de.se_rwth.commons.logging.Log;

import java.util.List;

public class CompleteVisibleModifierContradiction implements SDBasisASTSequenceDiagramCoCo {

  private static final String MESSAGE = "0xS0015: " +
        "The visible modifier of one of the objects is an direct contradiction to the complete modifier of the sd";

  @Override
  public void check(ASTSequenceDiagram node) {
    if(containsCompleteModifier(node.getSDModifierList())
            && containsAnObjectAnVisibleModifier(node.getSDObjectList())) {
      Log.warn(MESSAGE, node.get_SourcePositionStart());
    }
  }
  private boolean containsAnObjectAnVisibleModifier(List<ASTSDObject> node) {
    return node.stream().anyMatch(object -> containsVisibleModifier(object.getSDModifierList()));
  }
  private boolean containsCompleteModifier(List<ASTSDModifier> node) {
    return node.stream().anyMatch(modifier -> modifier instanceof ASTSDCompleteModifier);
  }
  private boolean containsVisibleModifier(List<ASTSDModifier> node) {
    return node.stream().anyMatch(modifier -> modifier instanceof ASTSDVisibleModifier);
  }
}
