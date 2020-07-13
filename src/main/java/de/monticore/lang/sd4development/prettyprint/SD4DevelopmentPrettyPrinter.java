package de.monticore.lang.sd4development.prettyprint;

import de.monticore.lang.sd4development._ast.*;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentInheritanceVisitor;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentVisitor;
import de.monticore.prettyprint.IndentPrinter;

public class SD4DevelopmentPrettyPrinter implements SD4DevelopmentInheritanceVisitor {

  private SD4DevelopmentVisitor realThis;

  private final IndentPrinter printer;

  public SD4DevelopmentPrettyPrinter(IndentPrinter printer) {
    this.realThis = this;
    this.printer = printer;
  }

  @Override
  public void setRealThis(SD4DevelopmentVisitor realThis) {
    this.realThis = realThis;
  }

  @Override
  public SD4DevelopmentVisitor getRealThis() {
    return realThis;
  }

  protected IndentPrinter getPrinter() {
    return printer;
  }

  @Override
  public void traverse(ASTSDNew node) {
    if (node.isPresentSDSource()) {
      node.getSDSource().accept(getRealThis());
    }
    getPrinter().print(" -> ");
    node.getDeclarationType().accept(getRealThis());
    getPrinter().print(" " + node.getName() + " = new ");
    node.getInitializationType().accept(getRealThis());
    node.getArguments().accept(getRealThis());
    if (node.isPresentSDActivityBar()) {
      node.getSDActivityBar().accept(getRealThis());
    } else {
      getPrinter().print(";");
    }
    getPrinter().println();
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
  public void traverse(ASTSDVariableDeclaration node) {
    getPrinter().print("let " );
    node.getMCType().accept(getRealThis());
    getPrinter().print(" " + node.getName() + " = ");
    node.getAssignment().accept(getRealThis());
    getPrinter().println(";");
  }

  @Override
  public void visit(ASTSDClass node) {
    getPrinter().print("class ");
  }

  @Override
  public void traverse(ASTSDEndCall node) {
    if (node.isPresentSDTarget()) {
      node.getSDTarget().accept(getRealThis());
    }
    getPrinter().print(" <- ");
    if (node.isPresentSDSource()) {
      node.getSDSource().accept(getRealThis());
    }
    getPrinter().print(" : ");
    node.getSDAction().accept(getRealThis());
    getPrinter().println(";");
  }


}
