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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import de.monticore.lang.sd._ast.ASTInteraction;
import de.monticore.lang.sd._ast.ASTMethodCall;
import de.monticore.lang.sd._ast.ASTObjectReference;
import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._symboltable.SDLanguage;

public class ObjectReferenceGetDeclarationTest {

	@Test
	public void test() throws IOException {
		SDLanguage lang = new SDLanguage();
		ASTSDArtifact sd = lang.loadModel("src/test/resources/examples/ast", "ObjectReferenceTest.sd");
		ASTInteraction i1 = sd.getSequenceDiagram().getSDElements().get(0).getInteraction().get();
		ASTObjectReference or1 = ((ASTMethodCall) i1).getTarget();
		ASTInteraction i2 = sd.getSequenceDiagram().getSDElements().get(1).getInteraction().get();
		ASTObjectReference or2 = ((ASTMethodCall) i2).getTarget();
		ASTInteraction i3 = sd.getSequenceDiagram().getSDElements().get(2).getInteraction().get();
		ASTObjectReference or3 = ((ASTMethodCall) i3).getTarget();
		ASTInteraction i4 = sd.getSequenceDiagram().getSDElements().get(3).getInteraction().get();
		ASTObjectReference or4 = ((ASTMethodCall) i4).getTarget();

		assertEquals("b", or1.getDeclaration().getName().get());
		assertEquals("B", or1.getDeclaration().getOfType().get());

		assertEquals("e", or2.getDeclaration().getName().get());
		assertEquals("E", or2.getDeclaration().getOfType().get());

		assertFalse(or3.getDeclaration().getName().isPresent());
		assertEquals("C", or3.getDeclaration().getOfType().get());

		assertTrue(or4.getDeclaration().isClass());
		assertEquals("D", or4.getDeclaration().getName().get());

	}

}
