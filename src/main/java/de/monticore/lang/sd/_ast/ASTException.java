package de.monticore.lang.sd._ast;

public class ASTException extends ASTExceptionTOP {

	public ASTException() {
		super();
	}

	public ASTException(ASTObjectReference left, ASTDashedArrow dashedArrow, ASTObjectReference right, String name,
			ASTArgs args) {
		super(left, dashedArrow, right, name, args);
	}

	public ASTObjectReference getSource() {
		if (dashedArrow == ASTDashedArrow.LEFT) {
			return right;
		} else {
			return left;
		}
	}

	public ASTObjectReference getTarget() {
		if (dashedArrow == ASTDashedArrow.LEFT) {
			return left;
		} else {
			return right;
		}
	}

}
