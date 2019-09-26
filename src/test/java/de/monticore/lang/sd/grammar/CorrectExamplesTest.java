/* (c) https://github.com/MontiCore/monticore */

package de.monticore.lang.sd.grammar;

import de.monticore.lang.sd._ast.ASTObjectDeclaration;
import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._ast.ASTSDElement;
import de.monticore.lang.sd._symboltable.SDLanguage;
import de.se_rwth.commons.logging.Log;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

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
		assertEquals(2, sd.getSequenceDiagram().getObjectDeclarationList().size());
		assertEquals(13, sd.getSequenceDiagram().getSDElementList().size());
	}

	@Test
	public void testExampleWithAllGrammarElements() throws IOException {
		// Load model
		SDLanguage lang = new SDLanguage();
		ASTSDArtifact sd = lang.loadModel("src/test/resources/examples/correct", "allGrammarElements.sd");

		// Traverse AST and check for correctness
		assertEquals(2, sd.getSequenceDiagram().getObjectDeclarationList().size());
		assertEquals(14, sd.getSequenceDiagram().getSDElementList().size());
	}

	@Test
	public void testCompletenessExample() throws IOException {
		// Load model
		SDLanguage lang = new SDLanguage();
		ASTSDArtifact sd = lang.loadModel("src/test/resources/examples/correct",
				"example_completeness_and_stereotypes.sd");

		// Traverse AST and check for correctness
		assertEquals(8, sd.getSequenceDiagram().getObjectDeclarationList().size());
		assertEquals(0, sd.getSequenceDiagram().getSDElementList().size());
		List<ASTObjectDeclaration> ods;
		ods = sd.getSequenceDiagram().getObjectDeclarationList();
		assertFalse(ods.get(0).getSDCompletenessOpt().isPresent());
		assertTrue(ods.get(1).getSDCompletenessOpt().get().isFree());
		assertTrue(ods.get(2).getSDCompletenessOpt().get().isFree());
		assertTrue(ods.get(3).getSDCompletenessOpt().get().isComplete());
		assertTrue(ods.get(4).getSDCompletenessOpt().get().isComplete());
		assertTrue(ods.get(5).getSDCompletenessOpt().get().isVisible());
		assertTrue(ods.get(6).getSDCompletenessOpt().get().isInitial());
		assertFalse(ods.get(7).getSDCompletenessOpt().isPresent());
		assertFalse(ods.get(7).getSDStereotypeList().isEmpty());
		assertEquals("someRandomStereotype", ods.get(7).getSDStereotypeList().get(0).getName());
	}

	@Test
	public void testActivityExample() throws IOException {
		// Load model
		SDLanguage lang = new SDLanguage();
		ASTSDArtifact sd = lang.loadModel("src/test/resources/examples/correct", "activities.sd");

		// Traverse AST and check for correctness
		assertEquals(2, sd.getSequenceDiagram().getObjectDeclarationList().size());
		assertEquals(1, sd.getSequenceDiagram().getSDElementList().size());

		ASTSDElement ac1 = sd.getSequenceDiagram().getSDElementList().get(0);
		assertTrue(ac1.getSDActivityOpt().isPresent());
		assertEquals(2, ac1.getSDActivityOpt().get().getSDElementList().size());

		ASTSDElement ac2 = ac1.getSDActivityOpt().get().getSDElementList().get(1);
		assertTrue(ac2.getSDActivityOpt().isPresent());
		assertEquals(2, ac2.getSDActivityOpt().get().getSDElementList().size());

		ASTSDElement ac3 = ac2.getSDActivityOpt().get().getSDElementList().get(1);
		assertTrue(ac3.getSDActivityOpt().isPresent());
		assertEquals(2, ac3.getSDActivityOpt().get().getSDElementList().size());

		ASTSDElement ac4 = ac3.getSDActivityOpt().get().getSDElementList().get(1);
		assertTrue(ac4.getSDActivityOpt().isPresent());
		assertEquals(0, ac4.getSDActivityOpt().get().getSDElementList().size());

	}

}
