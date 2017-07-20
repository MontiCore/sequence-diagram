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

import java.util.Optional;

import de.monticore.lang.sd._ast.ASTSDOCL;
import de.monticore.lang.sd._symboltable.ObjectDeclarationSymbol;
import de.monticore.symboltable.Scope;
import de.monticore.symboltable.Symbol;
import de.se_rwth.commons.logging.Log;

public class OCLContextDeclaredCoco implements SDASTSDOCLCoCo {

	@Override
	public void check(ASTSDOCL node) {
		if (node.contextIsPresent()) {
			String context = node.getContext().get();
			Scope sdScope = node.getEnclosingScope().get();
			Optional<Symbol> symbol = sdScope.resolveLocally(context, ObjectDeclarationSymbol.KIND);
			if (symbol.isPresent()) {
				return;
			}
			// Could not resolve context name
			Log.error(this.getClass().getSimpleName() + ": Context " + context + " is not a declared object.",
					node.get_SourcePositionStart());
		}

	}

}
