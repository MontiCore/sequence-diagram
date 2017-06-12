package de.monticore.lang.sd.grammar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import de.monticore.lang.sd._ast.ASTObjectDeclaration;
import de.monticore.lang.sd._ast.ASTSDArtifact;
import de.monticore.lang.sd._parser.SDParser;
import de.se_rwth.commons.logging.Log;

public class CorrectExamplesTest {

	private ASTSDArtifact loadModel(String modelName) throws IOException {
		// Load model
		Log.enableFailQuick(false);
		Path model = Paths.get(modelName);
		SDParser parser = new SDParser();
		Optional<ASTSDArtifact> sd = parser.parseSDArtifact(model.toString());
		assertFalse(parser.hasErrors());
		assertTrue(sd.isPresent());
		return sd.get();
	}

	@Test
	public void testExample() throws IOException {
		// Load model
		ASTSDArtifact sd = loadModel("src/test/resources/examples/correct/example.sd");

		// Traverse AST and check for correctness
		assertEquals(2, sd.getSequenceDiagram().getObjectDeclarations().size());
		assertEquals(13, sd.getSequenceDiagram().getSDElements().size());
	}

	@Test
	public void testExampleWithAllGrammarElements() throws IOException {
		// Load model
		ASTSDArtifact sd = loadModel("src/test/resources/examples/correct/allGrammarElements.sd");

		// Traverse AST and check for correctness
		assertEquals(2, sd.getSequenceDiagram().getObjectDeclarations().size());
		assertEquals(14, sd.getSequenceDiagram().getSDElements().size());
	}

	@Test
	public void testCompletenessExample() throws IOException {
		// Load model
		ASTSDArtifact sd = loadModel("src/test/resources/examples/correct/example_completeness_and_stereotypes.sd");

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

}
