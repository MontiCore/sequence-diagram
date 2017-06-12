package de.monticore.lang.sd._ast;

public class ASTReturn extends ASTReturnTOP {

	public ASTReturn() {
		super();
	}

	public ASTReturn(ASTObjectReference left, ASTDashedArrow dashedArrow, ASTObjectReference right,
			ASTReturnStatement returnStatement) {
		super(left, dashedArrow, right, returnStatement);
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
