/* (c) https://github.com/MontiCore/monticore */
package de.monticore.lang.sd4development.prettyprint;

import de.monticore.expressions.expressionsbasis._ast.ASTExpression;
import de.monticore.expressions.prettyprint.CommonExpressionsPrettyPrinter;
import de.monticore.expressions.prettyprint.ExpressionsBasisPrettyPrinter;
import de.monticore.lang.sd4development.SD4DevelopmentMill;
import de.monticore.lang.sd4development._ast.ASTSDEndCall;
import de.monticore.lang.sd4development._ast.ASTSDNew;
import de.monticore.lang.sd4development._ast.ASTSDVariableDeclaration;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentHandler;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentTraverser;
import de.monticore.lang.sdbasis._ast.ASTSDAction;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis._ast.ASTSDSource;
import de.monticore.lang.sdbasis._ast.ASTSDTarget;
import de.monticore.lang.sdbasis.prettyprint.SDBasisPrinter;
import de.monticore.literals.prettyprint.MCCommonLiteralsPrettyPrinter;
import de.monticore.ocl.oclexpressions.prettyprint.OCLExpressionsPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.prettyprint.UMLStereotypePrettyPrinter;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;

public class SD4DevelopmentPrettyPrinter implements SD4DevelopmentHandler {

  private final IndentPrinter printer;
  private SD4DevelopmentTraverser traverser;

  @Override
  public SD4DevelopmentTraverser getTraverser() {
    return traverser;
  }

  @Override
  public void setTraverser(SD4DevelopmentTraverser traverser) {
    this.traverser = traverser;
  }

  public SD4DevelopmentPrettyPrinter() {
    this(new IndentPrinter(), SD4DevelopmentMill.traverser());
  }

  public SD4DevelopmentPrettyPrinter(IndentPrinter printer, SD4DevelopmentTraverser t) {
    this.printer = printer;
    setTraverser(t);

    t.setSD4DevelopmentHandler(this);
    t.add4SD4Development(new SD4DevelopmentPrinter(printer));

    SDBasisPrinter sdbp = new SDBasisPrinter(printer);
    t.setSDBasisHandler(sdbp);
    t.add4SDBasis(sdbp);

    MCCommonLiteralsPrettyPrinter mcclv = new MCCommonLiteralsPrettyPrinter(printer);
    t.setMCCommonLiteralsHandler(mcclv);
    t.add4MCCommonLiterals(mcclv);

    MCBasicTypesPrettyPrinter mcbtpp = new MCBasicTypesPrettyPrinter(printer);
    t.setMCBasicTypesHandler(mcbtpp);
    t.add4MCBasicTypes(mcbtpp);

    UMLStereotypePrettyPrinter umlspp = new UMLStereotypePrettyPrinter(printer);
    t.setUMLStereotypeHandler(umlspp);
    t.add4UMLStereotype(umlspp);

    ExpressionsBasisPrettyPrinter ebpp = new ExpressionsBasisPrettyPrinter(printer);
    t.setExpressionsBasisHandler(ebpp);
    t.add4ExpressionsBasis(ebpp);

    CommonExpressionsPrettyPrinter cepp = new CommonExpressionsPrettyPrinter(printer);
    t.setCommonExpressionsHandler(cepp);
    t.add4CommonExpressions(cepp);

    OCLExpressionsPrettyPrinter oclepp = new OCLExpressionsPrettyPrinter(printer);
    t.setOCLExpressionsHandler(oclepp);
  }

  public String prettyPrint(ASTSDArtifact a) {
    printer.clearBuffer();
    a.accept(getTraverser());
    return printer.getContent();
  }

  public String prettyPrint(ASTSDEndCall a) {
    printer.clearBuffer();
    a.accept(getTraverser());
    return printer.getContent();
  }

  public String prettyPrint(ASTSDSource a) {
    printer.clearBuffer();
    a.accept(getTraverser());
    return printer.getContent();
  }

  public String prettyPrint(ASTSDTarget a) {
    printer.clearBuffer();
    a.accept(getTraverser());
    return printer.getContent();
  }

  public String prettyPrint(ASTSDAction a) {
    printer.clearBuffer();
    a.accept(getTraverser());
    return printer.getContent();
  }

  public String prettyPrint(ASTExpression a) {
    printer.clearBuffer();
    a.accept(getTraverser());
    return printer.getContent();
  }

  public IndentPrinter getPrinter() {
    return printer;
  }

  @Override
  public void traverse(ASTSDNew node) {
    if (node.isPresentSDSource()) {
      node.getSDSource().accept(getTraverser());
    }
    getPrinter().print(" -> ");
    node.getDeclarationType().accept(getTraverser());
    getPrinter().print(" " + node.getName() + " = new ");
    node.getInitializationType().accept(getTraverser());
    node.getArguments().accept(getTraverser());
    if (node.isPresentSDActivityBar()) {
      node.getSDActivityBar().accept(getTraverser());
    }
    else {
      getPrinter().print(";");
    }
    getPrinter().println();
  }

  @Override
  public void traverse(ASTSDVariableDeclaration node) {
    getPrinter().print("let ");
    node.getMCType().accept(getTraverser());
    getPrinter().print(" " + node.getName() + " = ");
    node.getAssignment().accept(getTraverser());
    getPrinter().println(";");
  }

  @Override
  public void traverse(ASTSDEndCall node) {
    if (node.isPresentSDTarget()) {
      node.getSDTarget().accept(getTraverser());
    }
    getPrinter().print(" <- ");
    if (node.isPresentSDSource()) {
      node.getSDSource().accept(getTraverser());
    }
    getPrinter().print(" : ");
    node.getSDAction().accept(getTraverser());
    getPrinter().println(";");
  }
}
