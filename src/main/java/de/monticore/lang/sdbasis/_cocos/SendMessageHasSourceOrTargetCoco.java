/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sdbasis._cocos;

import de.monticore.lang.sdbasis._ast.ASTSDObject;
import de.monticore.lang.sdbasis._ast.ASTSDSendMessage;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCObjectType;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.se_rwth.commons.logging.Log;

public class SendMessageHasSourceOrTargetCoco implements SDBasisASTSDSendMessageCoCo {

  private static final String MESSAGE_ERROR = "0xS0022: "
          + "The interaction has neither a source nor a target. At least one of them must be set.";

  @Override
  public void check(ASTSDSendMessage object) {
    if(!object.isPresentSDTarget() && !object.isPresentSDSource()) {
      Log.error(MESSAGE_ERROR);
    }
  }
}
