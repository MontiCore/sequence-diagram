/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development.prettyprint;

import de.monticore.lang.sd4development._ast.*;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentVisitor2;
import de.monticore.prettyprint.IndentPrinter;

public class SD4DevelopmentPrinter implements SD4DevelopmentVisitor2 {

  private final IndentPrinter printer;

  public SD4DevelopmentPrinter(IndentPrinter printer) {
    this.printer = printer;
  }

  public IndentPrinter getPrinter() {
    return printer;
  }

  @Override
  public void visit(ASTSDReturn node) {
    getPrinter().print("return ");
  }

  @Override
  public void visit(ASTSDThrow node) {
    getPrinter().print("throw ");
  }

  @Override
  public void visit(ASTSDCall node) {
    String sdCallPrint = "";
    sdCallPrint += node.isTrigger() ? "trigger " : "";
    sdCallPrint += node.isStatic() ? "static " : "";
    sdCallPrint += node.getName();
    getPrinter().print(sdCallPrint);
  }

  @Override
  public void visit(ASTSDIncompleteExpression node) {
    getPrinter().print("...");
  }

  @Override
  public void visit(ASTSDCondition node) {
    getPrinter().print("assert ");
  }

  @Override
  public void endVisit(ASTSDCondition node) {
    getPrinter().println(";");
  }

  @Override
  public void visit(ASTSDClass node) {
    getPrinter().print("class ");
  }
}
