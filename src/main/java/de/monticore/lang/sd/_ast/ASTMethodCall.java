/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd._ast;

import java.util.Optional;

public class ASTMethodCall extends ASTMethodCallTOP {

  public ASTMethodCall() {
    super();
  }

  public ASTMethodCall(ASTObjectReference left, ASTArrow arrow, ASTObjectReference right,
                         Optional<ASTSDStereotype> sDStereotype, ASTMethod method) {
    super(left, arrow, right, sDStereotype, method);
  }

  public ASTObjectReference getSource() {
    if (arrow == ASTArrow.LEFT) {
      return right;
    } else {
      return left;
    }
  }

  public ASTObjectReference getTarget() {
    if (arrow == ASTArrow.LEFT) {
      return left;
    } else {
      return right;
    }
  }
}
