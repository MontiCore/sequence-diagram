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

import de.monticore.lang.sd._ast.ASTException;
import de.monticore.lang.sd._ast.ASTInteraction;
import de.monticore.lang.sd._ast.ASTMethodCall;
import de.monticore.lang.sd._ast.ASTObjectDeclaration;
import de.monticore.lang.sd._ast.ASTObjectReference;
import de.monticore.lang.sd._ast.ASTReturn;
import de.monticore.lang.sd._ast.ASTSDElement;
import de.monticore.lang.sd._ast.ASTSequenceDiagram;
import de.monticore.lang.sd._cocos.SDASTSequenceDiagramCoCo;
import de.se_rwth.commons.logging.Log;

public class ReferencedObjectsDeclaredCoco implements SDASTSequenceDiagramCoCo {

	private List<String> declaredObjectNames;

	public ReferencedObjectsDeclaredCoco() {
		declaredObjectNames = new ArrayList<String>();
	}

	@Override
	public void check(ASTSequenceDiagram node) {
		// Object declarations: add to declared vars
		for (ASTObjectDeclaration od : node.getObjectDeclarations()) {
			declare(od);
		}
		// Object references: check if declared
		for (ASTSDElement e : node.getSDElements()) {
			if (e.getInteraction().isPresent()) {
				ASTInteraction interaction = e.getInteraction().get();
				if (interaction instanceof ASTMethodCall) {
					ASTMethodCall i = (ASTMethodCall) interaction;
					checkForDeclaration(i.getLeft());
					checkForDeclaration(i.getRight());
				} else if (interaction instanceof ASTReturn) {
					ASTReturn i = (ASTReturn) interaction;
					checkForDeclaration(i.getLeft());
					checkForDeclaration(i.getRight());
				} else if (interaction instanceof ASTException) {
					ASTException i = (ASTException) interaction;
					checkForDeclaration(i.getLeft());
					checkForDeclaration(i.getRight());
				}
			}
		}

	}

	private void declare(ASTObjectDeclaration od) {
		if (od.nameIsPresent()) {
			declaredObjectNames.add(od.getName().get());
		} else if (od.getOfType().isPresent()) {
			declaredObjectNames.add(od.getOfType().get());
		}
	}

	private void checkForDeclaration(ASTObjectReference o) {
		// Object declaration on its own
		if (o.inlineDeclarationIsPresent()) {
			declare(o.getInlineDeclaration().get());
		}

		// Simple reference
		else {
			String name = o.getName().get();
			if (!declaredObjectNames.contains(name)) {
				Log.error(this.getClass().getSimpleName() + ": Reference " + name + " refers to an undeclared object.",
						o.get_SourcePositionStart());
			}
		}
	}

}
