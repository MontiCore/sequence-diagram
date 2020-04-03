/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd._cocos;

import de.monticore.lang.sd._ast.*;
import de.se_rwth.commons.logging.Log;

import java.util.ArrayList;
import java.util.List;

public class ReferencedObjectsDeclaredCoco implements SDASTSequenceDiagramCoCo {

  private List<String> declaredObjectNames;

  public ReferencedObjectsDeclaredCoco() {
    declaredObjectNames = new ArrayList<String>();
  }

  @Override
  public void check(ASTSequenceDiagram node) {
    // Object declarations: add to declared vars
    for (ASTObjectDeclaration od : node.getObjectDeclarationList()) {
      declare(od);
    }
    // Object references: check if declared
    for (ASTSDElement e : node.getSDElementList()) {
      if (e.getInteractionOpt().isPresent()) {
        ASTInteraction interaction = e.getInteraction();
        if (interaction instanceof ASTMethodCall) {
          ASTMethodCall i = (ASTMethodCall) interaction;
          checkForDeclaration(i.getLeft());
          checkForDeclaration(i.getRight());
        } else if (interaction instanceof ASTReturn) {
          ASTReturn i = (ASTReturn) interaction;
          checkForDeclaration(i.getLeft());
          checkForDeclaration(i.getRight());
        } else if (interaction instanceof ASTException) {
          ASTException i = (ASTException) interaction;
          checkForDeclaration(i.getLeft());
          checkForDeclaration(i.getRight());
        }
      }
    }

  }

  private void declare(ASTObjectDeclaration od) {
    if (od.isPresentName()) {
      declaredObjectNames.add(od.getName());
    } else if (od.getOfTypeOpt().isPresent()) {
      declaredObjectNames.add(od.getOfTypeOpt().get());
    }
  }

  private void checkForDeclaration(ASTObjectReference o) {
    // Object declaration on its own
    if (o.isPresentInlineDeclaration()) {
      declare(o.getInlineDeclaration());
    }

    // Simple reference
    else {
      String name = o.getName();
      if (!declaredObjectNames.contains(name)) {
        Log.error(this.getClass().getSimpleName() + ": Reference " + name + " refers to an undeclared object.",
            o.get_SourcePositionStart());
      }
    }
  }

}
