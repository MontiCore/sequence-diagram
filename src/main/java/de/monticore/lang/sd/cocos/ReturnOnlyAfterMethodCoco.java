package de.monticore.lang.sd.cocos;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import de.monticore.lang.sd._ast.ASTInteraction;
import de.monticore.lang.sd._ast.ASTMethodCall;
import de.monticore.lang.sd._ast.ASTObject;
import de.monticore.lang.sd._ast.ASTReturn;
import de.monticore.lang.sd._cocos.SDASTInteractionCoCo;
import de.se_rwth.commons.logging.Log;

public class ReturnOnlyAfterMethodCoco implements SDASTInteractionCoCo {

	public static String CODE = "CoCo1";

	private Set<ASTMethodCall> openMethodCalls;

	public ReturnOnlyAfterMethodCoco() {
		openMethodCalls = new HashSet<ASTMethodCall>();
	}

	@Override
	public void check(ASTInteraction node) {
		Optional<ASTMethodCall> methodCall = node.getMethodCall();
		Optional<ASTReturn> returnInteraction = node.getReturn();
		if (methodCall.isPresent()) {
			check(methodCall.get());
		} else if (returnInteraction.isPresent()) {
			check(returnInteraction.get());
		}

	}

	private void check(ASTMethodCall node) {
		openMethodCalls.add(node);
	}

	private void check(ASTReturn node) {

		ASTMethodCall toBeRemoved = null;
		for (ASTMethodCall call : openMethodCalls) {
			// Response to open Method call : remove it
			ASTObject source = call.getSource();
			ASTObject target = call.getTarget();
			if (source.deepEquals(node.getTarget()) && target.deepEquals(node.getSource())) {
				toBeRemoved = call;
				break;
			}
		}

		if (toBeRemoved != null) {
			openMethodCalls.remove(toBeRemoved);
		} else {
			// No open method call: Coco violation
			String message = CODE + ": Return " + node.toString() + " occurs without previous call from "
					+ node.getSource().getName() + " to " + node.getTarget().getName();
			Log.error(message, node.get_SourcePositionStart());
		}

	}

}
