/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sdbasis._ast;

public interface ASTSDTarget extends ASTSDTargetTOP {
  void accept(InteractionEntityDispatcher dispatcher);
}
