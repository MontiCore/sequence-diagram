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

package de.monticore.lang.sd.ast;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import de.monticore.lang.sd._ast.ASTObjectDeclaration;
import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._symboltable.SDLanguage;

public class SDCompletenessEvaluationTest {

	@Test
	public void test() throws IOException {
		SDLanguage lang = new SDLanguage();
		ASTSDArtifact sd = lang.loadModel("src/test/resources/examples/correct",
				"example_completeness_and_stereotypes.sd");
		ASTObjectDeclaration od;

		od = sd.getSequenceDiagram().getObjectDeclarations().get(0);
		assertFalse(od.getSDCompleteness().isPresent());

		od = sd.getSequenceDiagram().getObjectDeclarations().get(1);
		assertTrue(od.getSDCompleteness().isPresent());
		assertFalse(od.getSDCompleteness().get().isComplete());
		assertTrue(od.getSDCompleteness().get().isFree());
		assertFalse(od.getSDCompleteness().get().isInitial());
		assertFalse(od.getSDCompleteness().get().isVisible());

		od = sd.getSequenceDiagram().getObjectDeclarations().get(2);
		assertTrue(od.getSDCompleteness().isPresent());
		assertFalse(od.getSDCompleteness().get().isComplete());
		assertTrue(od.getSDCompleteness().get().isFree());
		assertFalse(od.getSDCompleteness().get().isInitial());
		assertFalse(od.getSDCompleteness().get().isVisible());

		od = sd.getSequenceDiagram().getObjectDeclarations().get(3);
		assertTrue(od.getSDCompleteness().isPresent());
		assertTrue(od.getSDCompleteness().get().isComplete());
		assertFalse(od.getSDCompleteness().get().isFree());
		assertFalse(od.getSDCompleteness().get().isInitial());
		assertFalse(od.getSDCompleteness().get().isVisible());

		od = sd.getSequenceDiagram().getObjectDeclarations().get(4);
		assertTrue(od.getSDCompleteness().isPresent());
		assertTrue(od.getSDCompleteness().get().isComplete());
		assertFalse(od.getSDCompleteness().get().isFree());
		assertFalse(od.getSDCompleteness().get().isInitial());
		assertFalse(od.getSDCompleteness().get().isVisible());

		od = sd.getSequenceDiagram().getObjectDeclarations().get(5);
		assertTrue(od.getSDCompleteness().isPresent());
		assertFalse(od.getSDCompleteness().get().isComplete());
		assertFalse(od.getSDCompleteness().get().isFree());
		assertFalse(od.getSDCompleteness().get().isInitial());
		assertTrue(od.getSDCompleteness().get().isVisible());

		od = sd.getSequenceDiagram().getObjectDeclarations().get(6);
		assertTrue(od.getSDCompleteness().isPresent());
		assertFalse(od.getSDCompleteness().get().isComplete());
		assertFalse(od.getSDCompleteness().get().isFree());
		assertTrue(od.getSDCompleteness().get().isInitial());
		assertFalse(od.getSDCompleteness().get().isVisible());

	}

}
