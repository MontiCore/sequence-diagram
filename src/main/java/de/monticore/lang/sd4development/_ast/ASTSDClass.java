package de.monticore.lang.sd4development._ast;

import de.monticore.lang.sdbasis._ast.InteractionEntityDispatcher;

public class ASTSDClass extends ASTSDClassTOP {

  public void accept(InteractionEntityDispatcher dispatcher) {
    dispatcher.handle(this);
  }
}
