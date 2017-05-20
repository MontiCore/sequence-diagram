package de.monticore.lang.sd.cocos;

import de.monticore.lang.sd._ast.ASTConstructor;
import de.monticore.lang.sd._ast.ASTMethod;
import de.monticore.lang.sd._ast.ASTMethodCall;
import de.monticore.lang.sd._ast.ASTObjectReference;
import de.monticore.lang.sd._cocos.SDASTMethodCallCoCo;
import de.monticore.lang.sd.helper.SDHelper;
import de.se_rwth.commons.logging.Log;

public class InlineObjectDefinitionWithConstructorCoco implements SDASTMethodCallCoCo {

	@Override
	public void check(ASTMethodCall node) {

		// Get target and method
		SDHelper helper = new SDHelper();
		ASTObjectReference target = helper.getMethodCallTarget(node);
		ASTMethod method = node.getMethod();

		// method is constructor => inline object definition
		if (method instanceof ASTConstructor) {
			ASTConstructor constructor = (ASTConstructor) method;
			if (!target.objectDeclarationIsPresent()) {
				Log.error(this.getClass().getSimpleName()
						+ ": The constructor call does not define a new object with (name:Type) or (:Type) as target object.");
			} else {
				// Constructor name = type name?
				String typeName = target.getObjectDeclaration().get().getOfType().get();
				if (!constructor.getName().equals(typeName)) {
					Log.error(this.getClass().getSimpleName() + ": Type " + typeName + " does not match constructor "
							+ constructor.getName());
				}
			}
		}

	}

}