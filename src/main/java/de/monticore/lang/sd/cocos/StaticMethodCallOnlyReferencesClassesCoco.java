package de.monticore.lang.sd.cocos;

import de.monticore.lang.sd._ast.ASTMethodCall;
import de.monticore.lang.sd._ast.ASTObjectReference;
import de.monticore.lang.sd._cocos.SDASTMethodCallCoCo;
import de.monticore.lang.sd.prettyprint.SDPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.logging.Log;

public class StaticMethodCallOnlyReferencesClassesCoco implements SDASTMethodCallCoCo {

	@Override
	public void check(ASTMethodCall node) {
		ASTObjectReference target = node.getTarget();

		// static => Target must be a class
		if (node.getMethod().staticModifierIsPresent()) {
			if (target.getInlineDeclaration().isPresent()) {
				// not a class, since new object introduced
				Log.error(errorMessage(node, target, true), node.get_SourcePositionStart());
			} else if (!Character.isUpperCase(target.getName().get().charAt(0))) {
				// not a class since lower case;
				Log.error(errorMessage(node, target, false), node.get_SourcePositionStart());
			}
		}

		// target is a class => static
		if (!target.getInlineDeclaration().isPresent()) {
			if (Character.isUpperCase(target.getName().get().charAt(0))) {
				if (!node.getMethod().staticModifierIsPresent()) {
					Log.error(errorMessage2(target), target.get_SourcePositionStart());
				}
			}
		}
	}

	private String errorMessage(ASTMethodCall call, ASTObjectReference target, boolean objectDeclaration) {
		String message = this.getClass().getSimpleName() + ": ";
		message += "Static method call must refer to a class";
		if (objectDeclaration) {
			message += ", but refers to a new ObjectDeclaration ";
			SDPrettyPrinter pp = new SDPrettyPrinter(new IndentPrinter());
			pp.handle(target);
			message += pp.getPrinter().getContent();
		} else {
			message += ", but target is written in lower case: ";
			SDPrettyPrinter pp = new SDPrettyPrinter(new IndentPrinter());
			pp.handle(target);
			message += pp.getPrinter().getContent();
		}
		message += ".";
		return message;
	}

	private String errorMessage2(ASTObjectReference target) {
		String message = this.getClass().getSimpleName() + ": ";
		message += "Method calls targeting classes (here: class ";
		SDPrettyPrinter pp = new SDPrettyPrinter(new IndentPrinter());
		pp.handle(target);
		message += pp.getPrinter().getContent();
		message += ") must be marked as static";
		message += ".";
		return message;
	}

}
