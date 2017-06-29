package de.monticore.lang.sd.prettyprint;

import java.util.Iterator;

import de.monticore.common.prettyprint.CommonPrettyPrinterConcreteVisitor;
import de.monticore.lang.sd._ast.ASTArgs;
import de.monticore.lang.sd._ast.ASTArrow;
import de.monticore.lang.sd._ast.ASTDashedArrow;
import de.monticore.lang.sd._ast.ASTMethod;
import de.monticore.lang.sd._ast.ASTMethodCall;
import de.monticore.lang.sd._ast.ASTObjectDeclaration;
import de.monticore.lang.sd._ast.ASTObjectReference;
import de.monticore.lang.sd._ast.ASTParam;
import de.monticore.lang.sd._ast.ASTParamList;
import de.monticore.lang.sd._ast.ASTReturn;
import de.monticore.lang.sd._ast.ASTSDJava;
import de.monticore.lang.sd._ast.ASTSDNode;
import de.monticore.lang.sd._ast.ASTSDOCL;
import de.monticore.lang.sd._visitor.SDVisitor;
import de.monticore.prettyprint.IndentPrinter;

public class SDPrettyPrinter extends CommonPrettyPrinterConcreteVisitor implements SDVisitor {

	public SDPrettyPrinter(IndentPrinter printer) {
		super(printer);
	}

	public String prettyPrint(ASTSDNode node) {
		node.accept(this);
		String result = getPrinter().getContent();
		return result;
	}

	@Override
	public void handle(ASTObjectDeclaration od) {
		if (od.nameIsPresent()) {
			getPrinter().print(od.getName().get());
		}
		if (od.ofTypeIsPresent()) {
			getPrinter().print(":");
			getPrinter().print(od.getOfType().get());
		}
	}

	@Override
	public void handle(ASTObjectReference o) {
		if (o.inlineDeclarationIsPresent()) {
			ASTObjectDeclaration od = o.getInlineDeclaration().get();
			od.accept(realThis);
		} else {
			getPrinter().print(o.getName().get());
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
		if (method.staticModifierIsPresent()) {
			getPrinter().print("static ");
		}
		getPrinter().print(method.getName());
		if (method.argsIsPresent()) {
			method.getArgs().get().accept(realThis);
		}
	}

	@Override
	public void handle(ASTArgs args) {
		getPrinter().print("(");
		if (args.paramListIsPresent()) {
			args.getParamList().get().accept(realThis);
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
			Iterator<ASTParam> it = paramlist.getParams().iterator();
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
		if (param.nameIsPresent()) {
			getPrinter().print(param.getName().get());
		} else if (param.num_IntIsPresent()) {
			getPrinter().print(param.getNum_Int().get());
		} else if (param.stringIsPresent()) {
			getPrinter().print(param.getString().get());
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
		if (ocl.contextIsPresent()) {
			getPrinter().print(ocl.getContext().get());
			getPrinter().print(": ");
		}
		getPrinter().print("[");
		ocl.getOCLExpression().accept(realThis);
		getPrinter().print("]");
		getPrinter().print(">");
	}

	@Override
	public void handle(ASTSDJava java) {
		getPrinter().print("{{");
		// getPrinter().print(java.getString());
		getPrinter().print("}}");
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
	public SDVisitor getRealThis() {
		return realThis;
	}

}
