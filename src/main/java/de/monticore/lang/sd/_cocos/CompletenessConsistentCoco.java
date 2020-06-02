/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd._cocos;

import de.monticore.lang.sd._ast.ASTCompleteModifier;
import de.monticore.lang.sd._ast.ASTSDCompleteness;
import de.monticore.lang.sdbase._ast.ASTMatchModifier;
import de.monticore.lang.sdbase._ast.ASTSDObject;
import de.monticore.lang.sdbase._ast.ASTSequenceDiagram;
import de.monticore.lang.sdbase._cocos.SDBaseASTSequenceDiagramCoCo;
import de.monticore.umlmodifier._ast.ASTModifier;
import de.se_rwth.commons.logging.Log;

import java.util.Optional;

public class CompletenessConsistentCoco implements SDBaseASTSequenceDiagramCoCo {

  @Override
  public void check(ASTSequenceDiagram sd) {
    if(isComplete(sd)) {
      for (ASTSDObject obj : sd.getSDObjectList()) {
        check(obj);
      }
    }
  }
  private boolean isComplete(ASTSDObject obj) {
    return (obj.isPresentMatchModifier() && (obj.getMatchModifier() instanceof ASTCompleteModifier));
  }
  private boolean isComplete(ASTSequenceDiagram sd) {
    return (sd.isPresentMatchModifier() && (sd.getMatchModifier() instanceof ASTCompleteModifier));
  }
  private void check(ASTSDObject obj) {
      if (isComplete(obj)) {
        Log.error(
            this.getClass().getSimpleName()
                + ": Completeness of sequence diagram is set to complete, but completeness of object declaration is set to a different one.",
                obj.get_SourcePositionStart());
      }

  }

}
