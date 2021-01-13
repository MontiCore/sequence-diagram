/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sdbasis.prettyprint;

import de.monticore.lang.sdbasis._ast.*;
import de.monticore.lang.sdbasis._visitor.SDBasisHandler;
import de.monticore.lang.sdbasis._visitor.SDBasisTraverser;
import de.monticore.lang.sdbasis._visitor.SDBasisVisitor2;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;

public class SDBasisPrinter implements SDBasisVisitor2, SDBasisHandler {

  private final IndentPrinter printer;
  private SDBasisTraverser traverser;

  public SDBasisPrinter(IndentPrinter printer) {
    this.printer = printer;
  }

  @Override
  public SDBasisTraverser getTraverser() {
    return traverser;
  }

  @Override
  public void setTraverser(SDBasisTraverser traverser) {
    this.traverser = traverser;
  }

  protected IndentPrinter getPrinter() {
    return printer;
  }

  @Override
  public void traverse(ASTSDArtifact node) {
    if (node.isPresentPackageDeclaration()) {
      getPrinter().print("package ");
      node.getPackageDeclaration().accept(getTraverser());
      getPrinter().println(";");
    }
    for (ASTMCImportStatement i : node.getMCImportStatementList()) {
      i.accept(getTraverser());
    }
    node.getSequenceDiagram().accept(getTraverser());
  }

  @Override
  public void traverse(ASTSequenceDiagram node) {
    if (node.isPresentStereotype()) {
      node.getStereotype().accept(getTraverser());
    }
    for (ASTSDModifier modifier : node.getSDModifierList()) {
      modifier.accept(getTraverser());
    }
    getPrinter().println("sequencediagram " + node.getName() + " {");
    getPrinter().indent();
    for (ASTSDObject object : node.getSDObjectList()) {
      object.accept(getTraverser());
    }
    node.getSDBody().accept(getTraverser());

    getPrinter().unindent();
    getPrinter().println("}");
  }

  @Override
  public void traverse(ASTSDObject node) {
    if (node.isPresentStereotype()) {
      node.getStereotype().accept(getTraverser());
    }
    for (ASTSDModifier modifier : node.getSDModifierList()) {
      modifier.accept(getTraverser());
    }
    getPrinter().print(node.getName());
    if (node.isPresentMCObjectType()) {
      getPrinter().print(" : ");
      node.getMCObjectType().accept(getTraverser());
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
      node.getSDSource().accept(getTraverser());
    }
    getPrinter().print(" -> ");
    if (node.isPresentSDTarget()) {
      node.getSDTarget().accept(getTraverser());
    }
    getPrinter().print(" : ");
    if (null != node.getSDAction()) {
      node.getSDAction().accept(getTraverser());
    }
    if (node.isPresentSDActivityBar()) {
      node.getSDActivityBar().accept(getTraverser());
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
