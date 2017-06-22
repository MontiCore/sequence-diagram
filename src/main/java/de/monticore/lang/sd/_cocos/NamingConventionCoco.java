package de.monticore.lang.sd._cocos;

import de.monticore.lang.sd._ast.ASTObjectDeclaration;
import de.monticore.lang.sd._cocos.SDASTObjectDeclarationCoCo;
import de.monticore.lang.sd.prettyprint.SDPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.logging.Log;

public class NamingConventionCoco implements SDASTObjectDeclarationCoCo {

	@Override
	public void check(ASTObjectDeclaration node) {
		if (node.nameIsPresent()) {
			if (node.ofTypeIsPresent()) {
				// "object: Type"
				String name = node.getName().get();
				String type = node.getOfType().get();
				if (!Character.isLowerCase(name.charAt(0))) {
					Log.warn(errorMessage(node, 1, name), node.get_SourcePositionStart());
				}
				if (!Character.isUpperCase(type.charAt(0))) {
					Log.warn(errorMessage(node, 2, type), node.get_SourcePositionStart());
				}
			} else {
				String name = node.getName().get();
				if (node.isClass()) {
					// "class SomeClass"
					if (!Character.isUpperCase(name.charAt(0))) {
						Log.warn(errorMessage(node, 3, name), node.get_SourcePositionStart());
					}
				} else {
					// "object"
					if (!Character.isLowerCase(name.charAt(0))) {
						Log.warn(errorMessage(node, 1, name), node.get_SourcePositionStart());
					}
				}
			}
		} else if (node.ofTypeIsPresent()) {
			// ": Type"
			String typeName = node.getOfType().get();
			if (!Character.isUpperCase(typeName.charAt(0))) {
				Log.warn(errorMessage(node, 2, typeName), node.get_SourcePositionStart());
			}
		}

	}

	private String errorMessage(ASTObjectDeclaration node, int errorKind, String name) {
		String message = this.getClass().getSimpleName() + ": ";
		message += "Objectdeclaration ";
		SDPrettyPrinter pp = new SDPrettyPrinter(new IndentPrinter());
		pp.handle(node);
		message += pp.getPrinter().getContent();
		switch (errorKind) {
		case 1:
			message += " introduces an object with name " + name;
			message += " which should be written lower case by convention.";
			break;
		case 2:
			message += " introduces an object of type " + name;
			message += " which should be written upper case by convention.";
			break;
		case 3:
			message += " introduces a class " + name;
			message += " which should be written upper case by convention.";
			break;
		}
		return message;
	}

}
