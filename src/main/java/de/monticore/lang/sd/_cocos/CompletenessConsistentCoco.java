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

import de.monticore.lang.sd._ast.ASTObjectDeclaration;
import de.monticore.lang.sd._ast.ASTSDCompleteness;
import de.monticore.lang.sd._ast.ASTSequenceDiagram;
import de.monticore.lang.sd._cocos.SDASTSequenceDiagramCoCo;
import de.se_rwth.commons.logging.Log;

public class CompletenessConsistentCoco implements SDASTSequenceDiagramCoCo {

	private Optional<ASTSDCompleteness> globalCompleteness;

	@Override
	public void check(ASTSequenceDiagram node) {
		globalCompleteness = node.getSDCompleteness();
		for (ASTObjectDeclaration od : node.getObjectDeclarations()) {
			check(od);
		}
	}

	private void check(ASTObjectDeclaration od) {
		if (globalCompleteness.isPresent() && globalCompleteness.get().isComplete()) {
			if (od.getSDCompleteness().isPresent() && !od.getSDCompleteness().get().isComplete()) {
				Log.error(
						this.getClass().getSimpleName()
								+ ": Completeness of sequence diagram is set to complete, but completeness of object declaration is set to a different one.",
						od.get_SourcePositionStart());

			}
		}
	}

}
