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

package de.monticore.lang.sd._symboltable;

import java.util.Deque;

import de.monticore.symboltable.MutableScope;
import de.monticore.symboltable.ResolvingConfiguration;

public class SDSymbolTableCreator extends SDSymbolTableCreatorTOP {

	public SDSymbolTableCreator(ResolvingConfiguration resolvingConfig, Deque<MutableScope> scopeStack) {
		super(resolvingConfig, scopeStack);
	}

	public SDSymbolTableCreator(ResolvingConfiguration resolvingConfig, MutableScope enclosingScope) {
		super(resolvingConfig, enclosingScope);
	}

	@Override
	protected ObjectDeclarationSymbol create_ObjectDeclaration(de.monticore.lang.sd._ast.ASTObjectDeclaration ast) {
		String name = "";
		if (ast.getName().isPresent()) {
			name = ast.getName().get();
		} else if (ast.getOfType().isPresent()) {
			name = ast.getOfType().get();
		}
		return new ObjectDeclarationSymbol(name);
	}

}
