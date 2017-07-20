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

public class ASTException extends ASTExceptionTOP {

	public ASTException() {
		super();
	}

	public ASTException(ASTObjectReference left, ASTDashedArrow dashedArrow, ASTObjectReference right, String name,
			ASTArgs args) {
		super(left, dashedArrow, right, name, args);
	}

	public ASTObjectReference getSource() {
		if (dashedArrow == ASTDashedArrow.LEFT) {
			return right;
		} else {
			return left;
		}
	}

	public ASTObjectReference getTarget() {
		if (dashedArrow == ASTDashedArrow.LEFT) {
			return left;
		} else {
			return right;
		}
	}

}
