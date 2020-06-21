package de.monticore.lang.sd4development.prettyprint;

import de.monticore.MCCommonLiteralsPrettyPrinter;
import de.monticore.expressions.expressionsbasis._ast.ASTArguments;
import de.monticore.expressions.expressionsbasis._ast.ASTNameExpression;
import de.monticore.expressions.prettyprint.ExpressionsBasisPrettyPrinter;
import de.monticore.expressions.prettyprint.OCLExpressionsPrettyPrinter;
import de.monticore.lang.sd4development._ast.*;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentDelegatorVisitor;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentVisitor;
import de.monticore.lang.sdbasis._ast.*;
import de.monticore.lang.sdbasis.prettyprint.SDBasisPrettyPrinter;
import de.monticore.literals.mccommonliterals._ast.ASTStringLiteral;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.prettyprint.UMLStereotypePrettyPrinter;
import de.monticore.types.mcbasictypes._ast.ASTMCImportStatement;
import de.monticore.types.mcbasictypes._ast.ASTMCObjectType;
import de.monticore.types.mcbasictypes._visitor.MCBasicTypesVisitor;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;
import de.monticore.umlstereotype._ast.ASTStereotype;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class SD4DevelopmentPrettyPrinter extends SD4DevelopmentDelegatorVisitor {

  private final SD4DevelopmentDelegatorVisitor realThis;

  private final IndentPrinter printer;

  public SD4DevelopmentPrettyPrinter(IndentPrinter printer) {
    this.realThis = this;
    this.printer = printer;
    setSDBasisVisitor(new SDBasisPrettyPrinter(printer));
    setMCCommonLiteralsVisitor(new MCCommonLiteralsPrettyPrinter(printer));
    setOCLExpressionsVisitor(new OCLExpressionsPrettyPrinter(printer));
    setMCBasicTypesVisitor(new MCBasicTypesPrettyPrinter(printer));
    setUMLStereotypeVisitor(new UMLStereotypePrettyPrinter(printer));
    setExpressionsBasisVisitor(new ExpressionsBasisPrettyPrinter(printer));
  }

  public SD4DevelopmentPrettyPrinter() {
    this(new IndentPrinter());
  }

  protected IndentPrinter getPrinter() {
    return printer;
  }

  @Override
  public SD4DevelopmentDelegatorVisitor getRealThis() {
    return realThis;
  }

  @Override
  public void traverse(ASTSDNew node) {
    if (node.isPresentSDSource()) {
      node.getSDSource().accept(getRealThis());
    }
    getPrinter().print(" -> ");
    if (null != node.getDeclarationType()) {
      node.getDeclarationType().accept(getRealThis());
    }
    getPrinter().print(" " + node.getName() + " = new ");
    if (null != node.getInitializationType()) {
      node.getInitializationType().accept(getRealThis());
    }
    if (null != node.getArguments()) {
      node.getArguments().accept(getRealThis());
    }
    if (node.isPresentSDActivityBar()) {
      node.getSDActivityBar().accept(getRealThis());
    } else {
      getPrinter().print(";");
    }
    getPrinter().println();
  }

  @Override
  public void visit(ASTSDCall node) {
    String sdCallPrint = "";
    sdCallPrint += node.isTrigger() ? "trigger " : "";
    sdCallPrint += node.isStatic() ? "static " : "";
    sdCallPrint += node.getName();
    getPrinter().print(sdCallPrint);
  }

  public String prettyprint(ASTSDArtifact a) {
    a.accept(getRealThis());
    return printer.getContent();
  }

  @Override
  public void visit(ASTSDIncompleteExpression node) {
    getPrinter().print("...");
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
    if (null != node.getSDEndCallArrow()) {
      node.getSDEndCallArrow().accept(getRealThis());
    }
    getPrinter().print(" <- ");

    if (node.isPresentSDSource()) {
      node.getSDSource().accept(getRealThis());
    }
    getPrinter().print(" : ");
    if (null != node.getSDAction()) {
      node.getSDAction().accept(getRealThis());
    }
    getPrinter().println(";");

  }

}
