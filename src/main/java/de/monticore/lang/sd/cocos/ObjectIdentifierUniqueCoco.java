package de.monticore.lang.sd.cocos;

import java.util.ArrayList;
import java.util.List;

import de.monticore.lang.sd._ast.ASTObjectDeclaration;
import de.monticore.lang.sd._cocos.SDASTObjectDeclarationCoCo;
import de.monticore.lang.sd.prettyprint.SDPrettyPrinter;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.logging.Log;

public class ObjectIdentifierUniqueCoco implements SDASTObjectDeclarationCoCo {

	private List<String> usedIdentifiers;

	public ObjectIdentifierUniqueCoco() {
		usedIdentifiers = new ArrayList<String>();
	}

	@Override
	public void check(ASTObjectDeclaration node) {

		// Extract name
		String name = "";
		if (node.nameIsPresent()) {
			name = node.getName().get();
		} else if (node.ofTypeIsPresent()) {
			name = node.getOfType().get();
		}

		// No name for the object
		// (grammar should forbid this)
		if (name.equals("")) {
			String message = this.getClass().getSimpleName() + ": The object declaration ";
			SDPrettyPrinter pp = new SDPrettyPrinter(new IndentPrinter());
			pp.handle(node);
			message += pp.getPrinter().getContent();
			message += " is invalid as it does not give the object a name or a type.";
			Log.error(message);
			return;
		}

		// Name already in use?
		if (usedIdentifiers.contains(name)) {
			Log.error(this.getClass().getSimpleName() + ": Identifier " + name + " is already used.");
		}

		// Add name to used identifiers
		usedIdentifiers.add(name);

	}

}
