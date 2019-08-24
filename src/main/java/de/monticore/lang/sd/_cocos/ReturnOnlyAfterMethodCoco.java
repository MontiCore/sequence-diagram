/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd._cocos;

import java.util.HashSet;
import java.util.Set;

import de.monticore.lang.sd._ast.ASTInteraction;
import de.monticore.lang.sd._ast.ASTMethodCall;
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
			// Response to open Method call : remove it
			if (call.getSource().deepEquals(node.getTarget()) && call.getTarget().deepEquals(node.getSource())) {
				toBeRemoved = call;
				break;
			}
		}

		if (toBeRemoved != null) {
			openMethodCalls.remove(toBeRemoved);
		} else {
			// No open method call: Coco violation
			Log.warn(errorMessage(node), node.get_SourcePositionStart());
		}

	}

	private String errorMessage(ASTReturn node) {
		String message = this.getClass().getSimpleName() + ": ";
		message += "Return ";
		SDPrettyPrinter pp = new SDPrettyPrinter(new IndentPrinter());
		message += pp.prettyPrint(node);
		message += " occurs without previous call from ";
		pp = new SDPrettyPrinter(new IndentPrinter());
		message += pp.prettyPrint(node.getTarget());
		message += " to ";
		pp = new SDPrettyPrinter(new IndentPrinter());
		message += pp.prettyPrint(node.getSource());
		message += ".";
		return message;
	}

}
