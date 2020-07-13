/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd4development._cocos;

import de.monticore.lang.sd4development._ast.ASTSDEndCall;
import de.se_rwth.commons.logging.Log;

/**
 * Checks if an EndCall transition has at least a source or a target
 */
public class EndCallHasSourceOrTargetCoco implements SD4DevelopmentASTSDEndCallCoCo {

  private static final String MESSAGE_ERROR = "0xS0010: "
          + "The interaction has neither a source nor a target. At least one of them must be set.";
  
  @Override
  public void check(ASTSDEndCall object) {
    if(!object.isPresentSDTarget() && !object.isPresentSDSource()) {
      Log.error(MESSAGE_ERROR);
    }
  }
}
