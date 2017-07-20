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

package de.monticore.lang.sd._visitor;

import de.monticore.lang.sd._ast.ASTException;
import de.monticore.lang.sd._ast.ASTMethodCall;
import de.monticore.lang.sd._ast.ASTObjectDeclaration;
import de.monticore.lang.sd._ast.ASTReturn;
import de.monticore.lang.sd._ast.ASTSDJava;
import de.monticore.lang.sd._ast.ASTSDOCL;
import de.monticore.lang.sd._ast.ASTSequenceDiagram;

public abstract class SDTestVisitor implements SDVisitor {

	protected abstract void handleSequenceDiagram(ASTSequenceDiagram sd);

	protected abstract void handleObjectDeclaration(ASTObjectDeclaration od);

	protected abstract void handleMethodCall(ASTMethodCall call);

	protected abstract void handleReturn(ASTReturn ret);

	protected abstract void handleException(ASTException exception);

	protected abstract void handleJava(ASTSDJava java);

	protected abstract void handleOCL(ASTSDOCL ocl);

	protected abstract void executeAfterTraversal();

	protected void visitSD(ASTSequenceDiagram sd) {
		this.handleSequenceDiagram(sd);
		this.handle(sd);
	}

	@Override
	public void visit(ASTObjectDeclaration od) {
		handleObjectDeclaration(od);
	}

	@Override
	public void visit(ASTMethodCall call) {
		handleMethodCall(call);
	}

	@Override
	public void visit(ASTReturn ret) {
		handleReturn(ret);
	}

	@Override
	public void visit(ASTException exception) {
		handleException(exception);
	}

	@Override
	public void visit(ASTSDJava java) {
		handleJava(java);
	}

	@Override
	public void visit(ASTSDOCL ocl) {
		handleOCL(ocl);
	}

	@Override
	public void endVisit(ASTSequenceDiagram sd) {
		this.executeAfterTraversal();
	}
}
