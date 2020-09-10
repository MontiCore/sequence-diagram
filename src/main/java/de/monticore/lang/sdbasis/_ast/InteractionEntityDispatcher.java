/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sdbasis._ast;

import de.monticore.lang.sd4development._ast.ASTSDClass;

public interface InteractionEntityDispatcher {
  void handle(ASTSDObjectSource node);
  void handle(ASTSDObjectTarget node);
  void handle(ASTSDClass node);
}
