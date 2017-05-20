package de.monticore.lang.sd.prettyprint;

import de.monticore.common.prettyprint.CommonPrettyPrinterConcreteVisitor;
import de.monticore.lang.sd._ast.ASTArrow;
import de.monticore.lang.sd._ast.ASTDashedArrow;
import de.monticore.lang.sd._ast.ASTMethodCall;
import de.monticore.lang.sd._ast.ASTObjectDeclaration;
import de.monticore.lang.sd._ast.ASTObjectReference;
import de.monticore.lang.sd._ast.ASTReturn;
import de.monticore.lang.sd._visitor.SDVisitor;
import de.monticore.prettyprint.IndentPrinter;

public class SDPrettyPrinter extends CommonPrettyPrinterConcreteVisitor implements SDVisitor {

	public SDPrettyPrinter(IndentPrinter printer) {
		super(printer);
	}

	@Override
	public void handle(ASTObjectReference o) {
		if (o.objectDeclarationIsPresent()) {
			ASTObjectDeclaration od = o.getObjectDeclaration().get();
			od.accept(realThis);
		} else {
			getPrinter().print(o.getName().get());
		}
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
	public void handle(ASTReturn ret) {
		ret.getLeft().accept(realThis);
		if (ret.getDashedArrow() == ASTDashedArrow.LEFT) {
			getPrinter().print(" <-- ");
		} else {
			getPrinter().print(" --> ");
		}
		ret.getRight().accept(realThis);
	}

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
