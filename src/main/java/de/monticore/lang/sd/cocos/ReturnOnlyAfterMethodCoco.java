package de.monticore.lang.sd.cocos;

import java.util.HashSet;
import java.util.Set;

import de.monticore.lang.sd._ast.ASTArrow;
import de.monticore.lang.sd._ast.ASTDashedArrow;
import de.monticore.lang.sd._ast.ASTInteraction;
import de.monticore.lang.sd._ast.ASTMethodCall;
import de.monticore.lang.sd._ast.ASTObjectReference;
import de.monticore.lang.sd._ast.ASTReturn;
import de.monticore.lang.sd._cocos.SDASTInteractionCoCo;
import de.monticore.lang.sd.prettyprint.SDPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.logging.Log;

public class ReturnOnlyAfterMethodCoco implements SDASTInteractionCoCo {

	private Set<ASTMethodCall> openMethodCalls;

	public ReturnOnlyAfterMethodCoco() {
		openMethodCalls = new HashSet<ASTMethodCall>();
	}

	@Override
	public void check(ASTInteraction node) {
		if (node instanceof ASTMethodCall) {
			check((ASTMethodCall) node);
		} else if (node instanceof ASTReturn) {
			check((ASTReturn) node);
		}

	}

	private void check(ASTMethodCall node) {
		openMethodCalls.add(node);
	}

	private void check(ASTReturn node) {

		ASTMethodCall toBeRemoved = null;
		for (ASTMethodCall call : openMethodCalls) {
			// Determine sources and targets
			ASTObjectReference[] callST = getMethodCallSourceAndTarget(call);
			ASTObjectReference[] returnST = getReturnSourceAndTarget(node);
			// Response to open Method call : remove it
			if (callST[0].deepEquals(returnST[1]) && callST[1].deepEquals(returnST[0])) {
				toBeRemoved = call;
				break;
			}
		}

		if (toBeRemoved != null) {
			openMethodCalls.remove(toBeRemoved);
		} else {
			// No open method call: Coco violation
			Log.error(errorMessage(node), node.get_SourcePositionStart());
		}

	}

	private String errorMessage(ASTReturn node) {
		String message = this.getClass().getSimpleName() + ": ";
		message += "Return ";
		SDPrettyPrinter pp = new SDPrettyPrinter(new IndentPrinter());
		pp.handle(node);
		message += pp.getPrinter().getContent();
		message += " occurs without previous call from ";
		pp = new SDPrettyPrinter(new IndentPrinter());
		pp.handle(getReturnSourceAndTarget(node)[1]);
		message += pp.getPrinter().getContent();
		message += " to ";
		pp = new SDPrettyPrinter(new IndentPrinter());
		pp.handle(getReturnSourceAndTarget(node)[0]);
		message += pp.getPrinter().getContent();
		message += ".";
		return message;
	}

	private ASTObjectReference[] getMethodCallSourceAndTarget(ASTMethodCall call) {
		// Determine method call source & target
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

	private ASTObjectReference[] getReturnSourceAndTarget(ASTReturn node) {
		// Determine return source & target
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
