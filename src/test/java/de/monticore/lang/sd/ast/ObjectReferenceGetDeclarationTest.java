package de.monticore.lang.sd.ast;

import static org.junit.Assert.assertEquals;

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

		assertEquals("c", or2.getDeclaration().getName().get());
		assertEquals("C", or2.getDeclaration().getOfType().get());

		assertEquals("b", or1.getDeclaration().getName().get());
		assertEquals("B", or1.getDeclaration().getOfType().get());
	}

}
