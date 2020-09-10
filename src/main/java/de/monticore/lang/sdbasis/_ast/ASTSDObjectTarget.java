/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sdbasis._ast;

public class ASTSDObjectTarget extends ASTSDObjectTargetTOP {
  public void accept(InteractionEntityDispatcher dispatcher) {
    dispatcher.handle(this);
  }
}
