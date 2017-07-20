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

import java.util.List;

import de.monticore.types.types._ast.ASTQualifiedName;

public class ASTImportStatement extends ASTImportStatementTOP {

	public ASTImportStatement() {
		super();
	}

	public ASTImportStatement(ASTQualifiedName qualifiedName) {
		super(qualifiedName);
	}

	public String getPath() {
		String fqn = this.getQualifiedName().toString();
		fqn = fqn.substring(0, fqn.length() - getFileName().length());
		return fqn.replaceAll("\\.", "/");
	}

	public String getFileName() {
		List<String> parts = this.getQualifiedName().getParts();
		String fileName = parts.get(parts.size() - 2) + "." + parts.get(parts.size() - 1);
		return fileName;
	}

}
