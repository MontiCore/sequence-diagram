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
			message += pp.prettyPrint(node);
			message += " is invalid as it does not give the object a name or a type.";
			Log.error(message, node.get_SourcePositionStart());
			return;
		}

		// Name already in use?
		if (usedIdentifiers.contains(name)) {
			Log.error(this.getClass().getSimpleName() + ": Identifier " + name + " is already used.",
					node.get_SourcePositionStart());
		}

		// Add name to used identifiers
		usedIdentifiers.add(name);

	}

}
