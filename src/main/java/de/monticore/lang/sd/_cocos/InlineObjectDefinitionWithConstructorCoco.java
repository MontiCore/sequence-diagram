/*
 * ******************************************************************************
 * MontiCore Language Workbench, www.monticore.de
 * Copyright (c) 2017, MontiCore, All rights reserved.
 *
 * This project is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this project. If not, see <http://www.gnu.org/licenses/>.
 * ******************************************************************************
 */

package de.monticore.lang.sd._cocos;

import de.monticore.lang.sd._ast.ASTConstructor;
import de.monticore.lang.sd._ast.ASTMethod;
import de.monticore.lang.sd._ast.ASTMethodCall;
import de.monticore.lang.sd._ast.ASTObjectReference;
import de.monticore.lang.sd._cocos.SDASTMethodCallCoCo;
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
			if (!target.inlineDeclarationIsPresent()) {
				String name = constructor.getName();
				Log.error(
						this.getClass().getSimpleName() + ": The call of constructor " + name
								+ " does not define a new object of type " + name + " as target object.",
						constructor.get_SourcePositionStart());
			} else {
				// Constructor name = type name?
				String typeName = target.getInlineDeclaration().get().getOfType().get();
				if (!constructor.getName().equals(typeName)) {
					Log.error(this.getClass().getSimpleName() + ": Type " + typeName + " does not match constructor "
							+ constructor.getName(), constructor.get_SourcePositionStart());
				}
			}
		}

	}

}
