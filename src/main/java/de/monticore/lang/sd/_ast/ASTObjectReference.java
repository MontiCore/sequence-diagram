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

package de.monticore.lang.sd._ast;

import java.util.Optional;

import de.monticore.lang.sd._symboltable.ObjectDeclarationSymbol;
import de.monticore.symboltable.Scope;
import de.monticore.symboltable.Symbol;
import de.se_rwth.commons.logging.Log;

public class ASTObjectReference extends ASTObjectReferenceTOP {

	public ASTObjectReference() {
		super();
	}

	public ASTObjectReference(String name, ASTObjectDeclaration inlineDeclaration) {
		super(name, inlineDeclaration);
	}

	/**
	 * Gets the AST node of the object declaration in which the referenced
	 * object was initially declared.
	 * 
	 * Requires the symbol table to be built in advance.
	 * 
	 * @return
	 */
	public ASTObjectDeclaration getDeclaration() {
		// Local inline declaration: Just return it
		if (inlineDeclarationIsPresent()) {
			return getInlineDeclaration().get();
		}

		// Otherwise: Scan through AST
		Scope sdScope = getEnclosingScope().get();
		Optional<Symbol> symbol = sdScope.resolveLocally(getName().get(), ObjectDeclarationSymbol.KIND);
		if (!symbol.isPresent()) {
			Log.warn("Cannot resolve declaration to object " + getName().get());
		}
		return (ASTObjectDeclaration) symbol.get().getAstNode().get();
	}

}
