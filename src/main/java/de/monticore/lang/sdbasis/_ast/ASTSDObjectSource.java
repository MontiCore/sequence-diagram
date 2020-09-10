/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sdbasis._ast;

public class ASTSDObjectSource extends ASTSDObjectSourceTOP {
  public void accept(InteractionEntityDispatcher dispatcher) {
    dispatcher.handle(this);
  }
}
