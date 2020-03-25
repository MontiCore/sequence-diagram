/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd.prettyprint;

import de.monticore.ast.ASTNode;
import de.monticore.common.prettyprint.CommonPrettyPrinterConcreteVisitor;
import de.monticore.java.prettyprint.JavaDSLPrettyPrinter;
import de.monticore.lang.sd._ast.*;
import de.monticore.lang.sd._visitor.SDVisitor;
import de.monticore.prettyprint.IndentPrinter;

import java.util.Iterator;

public class SDPrettyPrinter extends CommonPrettyPrinterConcreteVisitor implements SDVisitor {

  public SDPrettyPrinter(IndentPrinter printer) {
    super(printer);
  }

  public String prettyPrint(ASTSDNode node) {
    node.accept(this);
    String result = getPrinter().getContent();
    getPrinter().clearBuffer();
    return result;
  }

  @Override
  public void handle(ASTObjectDeclaration od) {
    if (od.isPresentName()) {
      getPrinter().print(od.getName());
    }
    if (od.isPresentOfType()) {
      getPrinter().print(":");
      getPrinter().print(od.getOfType());
    }
  }

  @Override
  public void handle(ASTObjectReference o) {
    if (o.isPresentInlineDeclaration()) {
      ASTObjectDeclaration od = o.getInlineDeclaration();
      od.accept(realThis);
    } else {
      getPrinter().print(o.getName());
    }
  }

  @Override
  public void handle(ASTMethodCall call) {
    call.getLeft().accept(realThis);
    if (call.getArrow() == ASTArrow.LEFT) {
      getPrinter().print(" <- ");
    } else {
      getPrinter().print(" -> ");
    }
    call.getRight().accept(realThis);
  }

  @Override
  public void handle(ASTMethod method) {
    if (method.isPresentStaticModifier() && method.getStaticModifier().isStatic()) {
      getPrinter().print("static ");
    }
    getPrinter().print(method.getName());
    if (method.isPresentArgs()) {
      method.getArgs().accept(realThis);
    }
  }

  @Override
  public void handle(ASTArgs args) {
    getPrinter().print("(");
    if (args.isPresentParamList()) {
      args.getParamList().accept(realThis);
    }
    getPrinter().print(")");
  }

  @Override
  public void handle(ASTParamList paramlist) {
    if (paramlist.isIncomplete()) {
      // Incomplete
      getPrinter().print("...");
    } else {
      // Print param list
      Iterator<ASTParam> it = paramlist.getParamList().iterator();
      while (it.hasNext()) {
        ASTParam p = it.next();
        p.accept(realThis);
        if (it.hasNext()) {
          getPrinter().print(",");
        }
      }
    }
  }

  @Override
  public void handle(ASTParam param) {
    if (param.isPresentReference()) {
      getPrinter().print(param.getReference());
    } else if (param.isPresentNum_Int()) {
      getPrinter().print(param.getNum_Int());
    } else if (param.isPresentString()) {
      getPrinter().print("\"" + param.getString() + "\"");
    }
  }

  @Override
  public void handle(ASTReturn ret) {
    ret.getLeft().accept(realThis);
    if (ret.getDashedArrow() == ASTDashedArrow.LEFT) {
      getPrinter().print(" <-- ");
    } else {
      getPrinter().print(" --> ");
    }
    ret.getRight().accept(realThis);
  }

  @Override
  public void handle(ASTSDOCL ocl) {
    getPrinter().print("<");
    if (ocl.isPresentContext()) {
      getPrinter().print(ocl.getContext());
      getPrinter().print(": ");
    }
    getPrinter().print("[");
    //TODO:
        getPrinter().print("TODO: OCL");
//    ocl.getOCLExpression().accept(realThis);
    getPrinter().print("]");
    getPrinter().print(">");
  }

  @Override
  public void handle(ASTSDJava java) {
    getPrinter().print("java: {");
    JavaDSLPrettyPrinter pp = new JavaDSLPrettyPrinter(new IndentPrinter());
    String javaPP = pp.prettyprint(java.getBlockStatement());
    getPrinter().print(javaPP);
    getPrinter().print("}");
  }

  //TODO: Added for ?
    @Override
    public void endVisit(ASTNode node) {
        super.endVisit(node);
    }

    //TODO: Added for ?
    @Override
    public void visit(ASTNode node) {
        super.visit(node);
    }

    /*
   * Only visitor-related
   */

  private SDVisitor realThis = this;

  /**
   * @see de.monticore.lang.od._visitor.ODVisitor#setRealThis(de.monticore.lang.od._visitor.ODVisitor)
   */
  @Override
  public void setRealThis(SDVisitor realThis) {
    this.realThis = realThis;
  }

  /**
   * @see de.monticore.common.prettyprint.CommonPrettyPrinterConcreteVisitor#getRealThis()
   */
  @Override
  public SDPrettyPrinter getRealThis() {
    return  (SDPrettyPrinter) realThis;
  }


}
