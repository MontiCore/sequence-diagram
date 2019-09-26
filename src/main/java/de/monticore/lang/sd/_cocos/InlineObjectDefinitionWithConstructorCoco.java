/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd._cocos;

import de.monticore.lang.sd._ast.ASTConstructor;
import de.monticore.lang.sd._ast.ASTMethod;
import de.monticore.lang.sd._ast.ASTMethodCall;
import de.monticore.lang.sd._ast.ASTObjectReference;
import de.se_rwth.commons.logging.Log;

public class InlineObjectDefinitionWithConstructorCoco implements SDASTMethodCallCoCo {

	@Override
	public void check(ASTMethodCall node) {

		// Get target and method
		ASTObjectReference target = node.getTarget();
		ASTMethod method = node.getMethod();

		// method is constructor => inline object definition
		if (method instanceof ASTConstructor) {
			ASTConstructor constructor = (ASTConstructor) method;
			if (!target.isPresentInlineDeclaration()) {
				String name = constructor.getName();
				Log.error(
						this.getClass().getSimpleName() + ": The call of constructor " + name
								+ " does not define a new object of type " + name + " as target object.",
						constructor.get_SourcePositionStart());
			} else {
				// Constructor name = type name?
				String typeName = target.getInlineDeclaration().getOfType();
				if (!constructor.getName().equals(typeName)) {
					Log.error(this.getClass().getSimpleName() + ": Type " + typeName + " does not match constructor "
							+ constructor.getName(), constructor.get_SourcePositionStart());
				}
			}
		}

	}

}
