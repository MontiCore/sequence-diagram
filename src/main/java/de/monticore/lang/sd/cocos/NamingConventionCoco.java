package de.monticore.lang.sd.cocos;

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
				String name = node.getName().get();
				if (!Character.isLowerCase(name.charAt(0))) {
					Log.error(errorMessage(node, true, name));
				}
			} else {
				// Only name is given.
				// This could be a class, which would be okay
			}
		}
		if (node.ofTypeIsPresent()) {
			String typeName = node.getOfType().get();
			if (!Character.isUpperCase(typeName.charAt(0))) {
				Log.error(errorMessage(node, false, typeName));
			}
		}

	}

	private String errorMessage(ASTObjectDeclaration node, Boolean nameOrType, String name) {
		String message = this.getClass().getSimpleName() + ": ";
		message += "Objectdeclaration ";
		SDPrettyPrinter pp = new SDPrettyPrinter(new IndentPrinter());
		pp.handle(node);
		message += pp.getPrinter().getContent();
		if (nameOrType) {
			message += " introduces object name " + name;
			message += " which should be written lower case by convention.";
		} else {
			message += " references to type " + name;
			message += " which should be written upper case by convention.";
		}
		return message;
	}

}
