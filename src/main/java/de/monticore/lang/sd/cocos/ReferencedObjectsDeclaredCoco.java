package de.monticore.lang.sd.cocos;

import java.util.ArrayList;
import java.util.List;

import de.monticore.lang.sd._ast.ASTException;
import de.monticore.lang.sd._ast.ASTInteraction;
import de.monticore.lang.sd._ast.ASTMethodCall;
import de.monticore.lang.sd._ast.ASTObjectDeclaration;
import de.monticore.lang.sd._ast.ASTObjectReference;
import de.monticore.lang.sd._ast.ASTReturn;
import de.monticore.lang.sd._ast.ASTSDElement;
import de.monticore.lang.sd._cocos.SDASTSDElementCoCo;
import de.se_rwth.commons.logging.Log;

public class ReferencedObjectsDeclaredCoco implements SDASTSDElementCoCo {

	private List<String> declaredObjectNames;

	public ReferencedObjectsDeclaredCoco() {
		declaredObjectNames = new ArrayList<String>();
	}

	@Override
	public void check(ASTSDElement node) {
		if (node.getObjectDeclaration().isPresent()) {
			// Object declaration: add to declared vars
			declare(node.getObjectDeclaration().get());
		} else if (node.getInteraction().isPresent()) {
			// Object reference: check if declared
			ASTInteraction interaction = node.getInteraction().get();
			if (interaction instanceof ASTMethodCall) {
				ASTMethodCall i = (ASTMethodCall) interaction;
				checkForDeclaration(i.getLeft());
				checkForDeclaration(i.getRight());
			} else if (interaction instanceof ASTReturn) {
				ASTReturn i = (ASTReturn) interaction;
				checkForDeclaration(i.getLeft());
				checkForDeclaration(i.getRight());
			} else if (interaction instanceof ASTException) {
				ASTException i = (ASTException) interaction;
				checkForDeclaration(i.getLeft());
				checkForDeclaration(i.getRight());
			}
		}
	}

	private void declare(ASTObjectDeclaration od) {
		if (od.nameIsPresent()) {
			declaredObjectNames.add(od.getName().get());
		} else if (od.getOfType().isPresent()) {
			declaredObjectNames.add(od.getOfType().get());
		}
	}

	private void checkForDeclaration(ASTObjectReference o) {
		// Object declaration on its own
		if (o.objectDeclarationIsPresent()) {
			declare(o.getObjectDeclaration().get());
		}

		// Simple reference
		else {
			String name = o.getName().get();
			if (!declaredObjectNames.contains(name)) {
				Log.error(this.getClass().getSimpleName() + ": Reference " + name + " refers to an undeclared object.");
			}
		}
	}

}
