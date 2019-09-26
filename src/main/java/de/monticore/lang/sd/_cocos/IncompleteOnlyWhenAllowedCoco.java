/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd._cocos;

import de.monticore.lang.sd._ast.*;
import de.se_rwth.commons.logging.Log;

public class IncompleteOnlyWhenAllowedCoco implements SDASTSequenceDiagramCoCo {

	private Boolean sdIsComplete = false;

	@Override
	public void check(ASTSequenceDiagram node) {
		if (node.isPresentSDCompleteness()) {
			sdIsComplete = node.getSDCompleteness().isComplete();
		}

		for (ASTSDElement e : node.getSDElementList()) {
			if (e.isPresentInteraction()) {
				check(e.getInteraction());
			}
		}

	}

	private void check(ASTInteraction node) {

		// Method Call & Constructor
		if (node instanceof ASTMethodCall) {
			ASTMethodCall methodCall = (ASTMethodCall) node;
			if (sdIsComplete || islocallyComplete(methodCall.getSource())) {
				// No ... allowed
				ASTMethod method = methodCall.getMethod();
				if (method instanceof ASTConstructor) {
					ASTConstructor cons = (ASTConstructor) method;
					if (cons.isPresentArgs() && cons.getArgs().isPresentParamList()
							&& cons.getArgs().getParamList().isIncomplete()) {
						Log.error(
								this.getClass().getSimpleName()
										+ ": Cannot mark constructor parameter list as incomplete if matching is complete. ",
								node.get_SourcePositionStart());
					}
				} else {
					if (method.isPresentArgs() && method.getArgs().isPresentParamList()
							&& method.getArgs().getParamList().isIncomplete()) {
						Log.error(
								this.getClass().getSimpleName()
										+ ": Cannot mark method parameter list as incomplete if matching is complete. ",
								node.get_SourcePositionStart());
					}
				}
			}

		}

		// Exception
		if (node instanceof ASTException) {
			ASTException exception = (ASTException) node;
			if (sdIsComplete || islocallyComplete(exception.getSource())) {
				// No ... allowed
				if (exception.isPresentArgs() && exception.getArgs().isPresentParamList()
						&& exception.getArgs().getParamList().isIncomplete()) {
					Log.error(
							this.getClass().getSimpleName()
									+ ": Cannot mark exception parameter list as incomplete if matching is complete. ",
							node.get_SourcePositionStart());
				}

			}
		}

		// Return
		if (node instanceof ASTReturn) {
			ASTReturn ret = (ASTReturn) node;
			if (sdIsComplete || islocallyComplete(ret.getSource())) {
				// No ... allowed
				if (ret.isPresentReturnStatement() && ret.getReturnStatement().isPresentResult()
						&& ret.getReturnStatement().getResult().isIncomplete()) {
					Log.error(
							this.getClass().getSimpleName()
									+ ": Cannot mark return result as incomplete if matching is complete. ",
							node.get_SourcePositionStart());
				}

			}
		}

	}

	private boolean islocallyComplete(ASTObjectReference ref) {
		if (ref.getDeclaration() != null) {
			if (ref.getDeclaration().isPresentSDCompleteness()) {
				ASTSDCompleteness comp = ref.getDeclaration().getSDCompleteness();
				if (comp.isComplete()) {
					return true;
				}
			}
		}
		return false;
	}

}
