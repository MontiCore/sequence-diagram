/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4components._ast;

import de.monticore.symbols.compsymbols._symboltable.PortSymbol;

public class ASTSDPort extends ASTSDPortTOP {

  @Override
  protected void updatePortSymbol() {
    // override to remove lazy load
  }

  public void setPortSymbol(PortSymbol portSymbol) {
    this.portSymbol = portSymbol;
  }
}
