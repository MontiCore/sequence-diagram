package de.monticore.lang.sdbasis.prettyprint;

import de.monticore.lang.sdbasis._ast.*;
import de.monticore.lang.sdbasis._visitor.SDBasisVisitor;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;

import java.util.Iterator;

public class SDBasisPrettyPrinter implements SDBasisVisitor {

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
    for (ASTMCImportStatement i : node.getMCImportStatementList()) {
      i.accept(getRealThis());
    }
    node.getSequenceDiagram().accept(getRealThis());
  }

  @Override
  public void traverse(ASTSequenceDiagram node) {
    if (node.isPresentStereotype()) {
      node.getStereotype().accept(getRealThis());
    }
    {
      Iterator<de.monticore.lang.sdbasis._ast.ASTSDModifier> iter_sDModifiers = node.getSDModifierList().iterator();
      while (iter_sDModifiers.hasNext()) {
        iter_sDModifiers.next().accept(getRealThis());
      }
    }
    getPrinter().println("sequencediagram " + node.getName() + " {");
    getPrinter().indent();
    {
      Iterator<de.monticore.lang.sdbasis._ast.ASTSDObject> iter_sDObjects = node.getSDObjectList().iterator();
      while (iter_sDObjects.hasNext()) {
        iter_sDObjects.next().accept(getRealThis());
      }
    }
    if (null != node.getSDBody()) {
      node.getSDBody().accept(getRealThis());
    }

    // although we generally assume that the symbol table is always available,
    // there are cases, where this is not true (for example construction of the
    // symbol table itself. Thus, the null-check is necessary.
    if (node.getSpannedScope() != null) {
      node.getSpannedScope().accept(getRealThis());
    }

    getPrinter().unindent();
    getPrinter().println("}");
  }

  @Override
  public void endVisit(ASTSequenceDiagram node) {
    getPrinter().unindent();
    getPrinter().println("}");
  }

  @Override
  public void traverse(ASTSDObject node) {
    if (node.isPresentStereotype()) {
      node.getStereotype().accept(getRealThis());
    }
    {
      Iterator<de.monticore.lang.sdbasis._ast.ASTSDModifier> iter_sDModifiers = node.getSDModifierList().iterator();
      while (iter_sDModifiers.hasNext()) {
        iter_sDModifiers.next().accept(getRealThis());
      }
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
    getPrinter().print("}");
    getPrinter().unindent();
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
