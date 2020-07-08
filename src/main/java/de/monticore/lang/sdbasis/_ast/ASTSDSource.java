package de.monticore.lang.sdbasis._ast;

public interface ASTSDSource extends ASTSDSourceTOP {
  void accept(InteractionEntityDispatcher dispatcher);
}
