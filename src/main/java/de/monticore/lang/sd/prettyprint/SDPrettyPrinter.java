package de.monticore.lang.sd.prettyprint;

import de.monticore.common.prettyprint.CommonPrettyPrinterConcreteVisitor;
import de.monticore.lang.sd._visitor.SDVisitor;
import de.monticore.prettyprint.IndentPrinter;

public class SDPrettyPrinter extends CommonPrettyPrinterConcreteVisitor implements SDVisitor {

	public SDPrettyPrinter(IndentPrinter printer) {
		super(printer);
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
