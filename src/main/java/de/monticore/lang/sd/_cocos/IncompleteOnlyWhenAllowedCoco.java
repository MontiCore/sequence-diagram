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
import de.monticore.lang.sd._ast.ASTException;
import de.monticore.lang.sd._ast.ASTInteraction;
import de.monticore.lang.sd._ast.ASTMethod;
import de.monticore.lang.sd._ast.ASTMethodCall;
import de.monticore.lang.sd._ast.ASTObjectReference;
import de.monticore.lang.sd._ast.ASTReturn;
import de.monticore.lang.sd._ast.ASTSDCompleteness;
import de.monticore.lang.sd._ast.ASTSDElement;
import de.monticore.lang.sd._ast.ASTSequenceDiagram;
import de.se_rwth.commons.logging.Log;

public class IncompleteOnlyWhenAllowedCoco implements SDASTSequenceDiagramCoCo {

	private Boolean sdIsComplete = false;

	@Override
	public void check(ASTSequenceDiagram node) {
		if (node.sDCompletenessIsPresent()) {
			sdIsComplete = node.getSDCompleteness().get().isComplete();
		}

		for (ASTSDElement e : node.getSDElements()) {
			if (e.getInteraction().isPresent()) {
				check(e.getInteraction().get());
			}
		}

	}

	private void check(ASTInteraction node) {

		// Method Call & Constructor
		if (node instanceof ASTMethodCall) {
			ASTMethodCall methodCall = (ASTMethodCall) node;
			if (sdIsComplete || islocallyComplete(methodCall.getSource())) {
				// No ... allowed
				ASTMethod method = methodCall.getMethod();
				if (method instanceof ASTConstructor) {
					ASTConstructor cons = (ASTConstructor) method;
					if (cons.argsIsPresent() && cons.getArgs().get().paramListIsPresent()
							&& cons.getArgs().get().getParamList().get().isIncomplete()) {
						Log.error(
								this.getClass().getSimpleName()
										+ ": Cannot mark constructor parameter list as incomplete if matching is complete. ",
								node.get_SourcePositionStart());
					}
				} else {
					if (method.argsIsPresent() && method.getArgs().get().paramListIsPresent()
							&& method.getArgs().get().getParamList().get().isIncomplete()) {
						Log.error(
								this.getClass().getSimpleName()
										+ ": Cannot mark method parameter list as incomplete if matching is complete. ",
								node.get_SourcePositionStart());
					}
				}
			}

		}

		// Exception
		if (node instanceof ASTException) {
			ASTException exception = (ASTException) node;
			if (sdIsComplete || islocallyComplete(exception.getSource())) {
				// No ... allowed
				if (exception.argsIsPresent() && exception.getArgs().get().paramListIsPresent()
						&& exception.getArgs().get().getParamList().get().isIncomplete()) {
					Log.error(
							this.getClass().getSimpleName()
									+ ": Cannot mark exception parameter list as incomplete if matching is complete. ",
							node.get_SourcePositionStart());
				}

			}
		}

		// Return
		if (node instanceof ASTReturn) {
			ASTReturn ret = (ASTReturn) node;
			if (sdIsComplete || islocallyComplete(ret.getSource())) {
				// No ... allowed
				if (ret.returnStatementIsPresent() && ret.getReturnStatement().get().resultIsPresent()
						&& ret.getReturnStatement().get().getResult().get().isIncomplete()) {
					Log.error(
							this.getClass().getSimpleName()
									+ ": Cannot mark return result as incomplete if matching is complete. ",
							node.get_SourcePositionStart());
				}

			}
		}

	}

	private boolean islocallyComplete(ASTObjectReference ref) {
		if (ref.getDeclaration() != null) {
			if (ref.getDeclaration().sDCompletenessIsPresent()) {
				ASTSDCompleteness comp = ref.getDeclaration().getSDCompleteness().get();
				if (comp.isComplete()) {
					return true;
				}
			}
		}
		return false;
	}

}
