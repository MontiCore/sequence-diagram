package de.monticore.lang.sd4development.prettyprint;

import de.monticore.MCCommonLiteralsPrettyPrinter;
import de.monticore.expressions.prettyprint.ExpressionsBasisPrettyPrinter;
import de.monticore.expressions.prettyprint.OCLExpressionsPrettyPrinter;
import de.monticore.lang.sd4development._visitor.SD4DevelopmentDelegatorVisitor;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.lang.sdbasis.prettyprint.SDBasisPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.monticore.prettyprint.UMLStereotypePrettyPrinter;
import de.monticore.types.prettyprint.MCBasicTypesPrettyPrinter;

public class SD4DevelopmentDelegatorPrettyPrinter extends SD4DevelopmentDelegatorVisitor {

  private final SD4DevelopmentDelegatorVisitor realThis;

  private final IndentPrinter printer;

  public SD4DevelopmentDelegatorPrettyPrinter(IndentPrinter printer) {
    this.realThis = this;
    this.printer = printer;
    setSD4DevelopmentVisitor(new SD4DevelopmentPrettyPrinter(printer));
    setSDBasisVisitor(new SDBasisPrettyPrinter(printer));
    setMCCommonLiteralsVisitor(new MCCommonLiteralsPrettyPrinter(printer));
    setOCLExpressionsVisitor(new OCLExpressionsPrettyPrinter(printer));
    setMCBasicTypesVisitor(new MCBasicTypesPrettyPrinter(printer));
    setUMLStereotypeVisitor(new UMLStereotypePrettyPrinter(printer));
    setExpressionsBasisVisitor(new ExpressionsBasisPrettyPrinter(printer));
  }

  public SD4DevelopmentDelegatorPrettyPrinter() {
    this(new IndentPrinter());
  }

  @Override
  public SD4DevelopmentDelegatorVisitor getRealThis() {
    return realThis;
  }

  public String prettyPrint(ASTSDArtifact a) {
    printer.clearBuffer();
    a.accept(getRealThis());
    return printer.getContent();
  }
}
