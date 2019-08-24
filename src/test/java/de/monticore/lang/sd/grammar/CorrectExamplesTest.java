/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd.grammar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import de.monticore.lang.sd._ast.ASTObjectDeclaration;
import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._ast.ASTSDElement;
import de.monticore.lang.sd._symboltable.SDLanguage;
import de.se_rwth.commons.logging.Log;

public class CorrectExamplesTest {

	@BeforeClass
	public static void setUp() {
		Log.enableFailQuick(true);
	}

	@Test
	public void testExample() throws IOException {
		// Load model
		SDLanguage lang = new SDLanguage();
		ASTSDArtifact sd = lang.loadModel("src/test/resources/examples/correct", "example.sd");

		// Traverse AST and check for correctness
		assertEquals(2, sd.getSequenceDiagram().getObjectDeclarations().size());
		assertEquals(13, sd.getSequenceDiagram().getSDElements().size());
	}

	@Test
	public void testExampleWithAllGrammarElements() throws IOException {
		// Load model
		SDLanguage lang = new SDLanguage();
		ASTSDArtifact sd = lang.loadModel("src/test/resources/examples/correct", "allGrammarElements.sd");

		// Traverse AST and check for correctness
		assertEquals(2, sd.getSequenceDiagram().getObjectDeclarations().size());
		assertEquals(14, sd.getSequenceDiagram().getSDElements().size());
	}

	@Test
	public void testCompletenessExample() throws IOException {
		// Load model
		SDLanguage lang = new SDLanguage();
		ASTSDArtifact sd = lang.loadModel("src/test/resources/examples/correct",
				"example_completeness_and_stereotypes.sd");

		// Traverse AST and check for correctness
		assertEquals(8, sd.getSequenceDiagram().getObjectDeclarations().size());
		assertEquals(0, sd.getSequenceDiagram().getSDElements().size());
		List<ASTObjectDeclaration> ods;
		ods = sd.getSequenceDiagram().getObjectDeclarations();
		assertFalse(ods.get(0).getSDCompleteness().isPresent());
		assertTrue(ods.get(1).getSDCompleteness().get().isFree());
		assertTrue(ods.get(2).getSDCompleteness().get().isFree());
		assertTrue(ods.get(3).getSDCompleteness().get().isComplete());
		assertTrue(ods.get(4).getSDCompleteness().get().isComplete());
		assertTrue(ods.get(5).getSDCompleteness().get().isVisible());
		assertTrue(ods.get(6).getSDCompleteness().get().isInitial());
		assertFalse(ods.get(7).getSDCompleteness().isPresent());
		assertFalse(ods.get(7).getSDStereotypes().isEmpty());
		assertEquals("someRandomStereotype", ods.get(7).getSDStereotypes().get(0).getName());
	}

	@Test
	public void testActivityExample() throws IOException {
		// Load model
		SDLanguage lang = new SDLanguage();
		ASTSDArtifact sd = lang.loadModel("src/test/resources/examples/correct", "activities.sd");

		// Traverse AST and check for correctness
		assertEquals(2, sd.getSequenceDiagram().getObjectDeclarations().size());
		assertEquals(1, sd.getSequenceDiagram().getSDElements().size());

		ASTSDElement ac1 = sd.getSequenceDiagram().getSDElements().get(0);
		assertTrue(ac1.getSDActivity().isPresent());
		assertEquals(2, ac1.getSDActivity().get().getSDElements().size());

		ASTSDElement ac2 = ac1.getSDActivity().get().getSDElements().get(1);
		assertTrue(ac2.getSDActivity().isPresent());
		assertEquals(2, ac2.getSDActivity().get().getSDElements().size());

		ASTSDElement ac3 = ac2.getSDActivity().get().getSDElements().get(1);
		assertTrue(ac3.getSDActivity().isPresent());
		assertEquals(2, ac3.getSDActivity().get().getSDElements().size());

		ASTSDElement ac4 = ac3.getSDActivity().get().getSDElements().get(1);
		assertTrue(ac4.getSDActivity().isPresent());
		assertEquals(0, ac4.getSDActivity().get().getSDElements().size());

	}

}
