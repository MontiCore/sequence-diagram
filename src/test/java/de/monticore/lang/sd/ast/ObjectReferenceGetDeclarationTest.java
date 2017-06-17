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
