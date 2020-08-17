package de.monticore.lang.sdbasis.prettyprint;

import de.monticore.lang.sdbasis._ast.*;
import de.monticore.lang.sdbasis._visitor.SDBasisInheritanceVisitor;
import de.monticore.lang.sdbasis._visitor.SDBasisVisitor;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;

public class SDBasisPrettyPrinter implements SDBasisInheritanceVisitor {

  private SDBasisVisitor realThis;

  private final IndentPrinter printer;

  public SDBasisPrettyPrinter(IndentPrinter printer) {
    this.realThis = this;
    this.printer = printer;
  }

  protected IndentPrinter getPrinter() {
    return printer;
  }

  @Override
  public void setRealThis(SDBasisVisitor realThis) {
    this.realThis = realThis;
  }

  @Override
  public SDBasisVisitor getRealThis() {
    return realThis;
  }

  @Override
  public void traverse(ASTSDArtifact node) {
    if (node.isPresentPackageDeclaration()) {
      getPrinter().print("package ");
      node.getPackageDeclaration().accept(getRealThis());
      getPrinter().println(";");
    }
    for (ASTMCImportStatement i : node.getMCImportStatementsList()) {
      i.accept(getRealThis());
    }
    node.getSequenceDiagram().accept(getRealThis());
  }

  @Override
  public void traverse(ASTSequenceDiagram node) {
    if (node.isPresentStereotype()) {
      node.getStereotype().accept(getRealThis());
    }
    for (ASTSDModifier modifier : node.getSDModifiersList()) {
      modifier.accept(getRealThis());
    }
    getPrinter().println("sequencediagram " + node.getName() + " {");
    getPrinter().indent();
    for (ASTSDObject object : node.getSDObjectsList()) {
      object.accept(getRealThis());
    }
    node.getSDBody().accept(getRealThis());

    getPrinter().unindent();
    getPrinter().println("}");
  }

  @Override
  public void traverse(ASTSDObject node) {
    if (node.isPresentStereotype()) {
      node.getStereotype().accept(getRealThis());
    }
    for (ASTSDModifier modifier : node.getSDModifiersList()) {
      modifier.accept(getRealThis());
    }
    getPrinter().print(node.getName());
    if (node.isPresentMCObjectType()) {
      getPrinter().print(" : ");
      node.getMCObjectType().accept(getRealThis());
    }
    getPrinter().println(";");
  }

  @Override
  public void visit(ASTSDObjectSource node) {
    getPrinter().print(node.getName());
  }

  @Override
  public void visit(ASTSDObjectTarget node) {
    getPrinter().print(node.getName());
  }

  @Override
  public void traverse(ASTSDSendMessage node) {
    if (node.isPresentSDSource()) {
      node.getSDSource().accept(getRealThis());
    }
    getPrinter().print(" -> ");
    if (node.isPresentSDTarget()) {
      node.getSDTarget().accept(getRealThis());
    }
    getPrinter().print(" : ");
    if (null != node.getSDAction()) {
      node.getSDAction().accept(getRealThis());
    }
    if (node.isPresentSDActivityBar()) {
      node.getSDActivityBar().accept(getRealThis());
    } else {
      getPrinter().print(";");
    }
    getPrinter().println();
  }

  @Override
  public void visit(ASTSDActivityBar node) {
    getPrinter().println(" {");
    getPrinter().indent();
  }

  @Override
  public void endVisit(ASTSDActivityBar node) {
    getPrinter().unindent();
    getPrinter().print("}");
  }

  @Override
  public void visit(ASTSDCompleteModifier node) {
    getPrinter().print("complete ");
  }

  @Override
  public void visit(ASTSDFreeModifier node) {
    getPrinter().print("free ");
  }

  @Override
  public void visit(ASTSDInitialModifier node) {
    getPrinter().print("initial ");
  }

  @Override
  public void visit(ASTSDVisibleModifier node) {
    getPrinter().print("visible ");
  }
}
