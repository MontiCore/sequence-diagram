package de.monticore.lang.sd.helper;

import de.monticore.lang.sd._ast.ASTArrow;
import de.monticore.lang.sd._ast.ASTDashedArrow;
import de.monticore.lang.sd._ast.ASTMethodCall;
import de.monticore.lang.sd._ast.ASTObjectReference;
import de.monticore.lang.sd._ast.ASTReturn;

public class SDHelper {

	public ASTObjectReference getMethodCallSource(ASTMethodCall call) {
		return getMethodCallSourceAndTarget(call)[0];
	}

	public ASTObjectReference getMethodCallTarget(ASTMethodCall call) {
		return getMethodCallSourceAndTarget(call)[1];
	}

	public ASTObjectReference getReturnSource(ASTReturn node) {
		return getReturnSourceAndTarget(node)[0];
	}

	public ASTObjectReference getReturnTarget(ASTReturn node) {
		return getReturnSourceAndTarget(node)[1];
	}

	public ASTObjectReference[] getMethodCallSourceAndTarget(ASTMethodCall call) {
		ASTObjectReference left = call.getLeft();
		ASTObjectReference right = call.getRight();
		ASTObjectReference callSource, callTarget;
		if (call.getArrow() == ASTArrow.LEFT) {
			callSource = right;
			callTarget = left;
		} else {
			callSource = left;
			callTarget = right;
		}
		return new ASTObjectReference[] { callSource, callTarget };
	}

	public ASTObjectReference[] getReturnSourceAndTarget(ASTReturn node) {
		ASTObjectReference left = node.getLeft();
		ASTObjectReference right = node.getRight();
		ASTObjectReference returnSource, returnTarget;
		if (node.getDashedArrow() == ASTDashedArrow.LEFT) {
			returnSource = right;
			returnTarget = left;
		} else {
			returnSource = left;
			returnTarget = right;
		}
		return new ASTObjectReference[] { returnSource, returnTarget };
	}

}
