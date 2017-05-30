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
import de.monticore.lang.sd._ast.ASTSDCompilationUnit;
import de.monticore.lang.sd._parser.SDParser;
import de.se_rwth.commons.logging.Log;

public class CorrectExamplesTest {

	private ASTSDCompilationUnit loadModel(String modelName) throws IOException {
		// Load model
		Log.enableFailQuick(false);
		Path model = Paths.get(modelName);
		SDParser parser = new SDParser();
		Optional<ASTSDCompilationUnit> sd = parser.parseSDCompilationUnit(model.toString());
		assertFalse(parser.hasErrors());
		assertTrue(sd.isPresent());
		return sd.get();
	}

	@Test
	public void testExample() throws IOException {
		// Load model
		ASTSDCompilationUnit sd = loadModel("src/test/resources/examples/correct/example.sd");

		// Traverse AST and check for correctness
		assertEquals(2, sd.getSequenceDiagram().getObjectDeclarations().size());
		assertEquals(13, sd.getSequenceDiagram().getSDElements().size());
	}

	@Test
	public void testCompletenessExample() throws IOException {
		// Load model
		ASTSDCompilationUnit sd = loadModel(
				"src/test/resources/examples/correct/example_completeness_and_stereotypes.sd");

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
		assertTrue(ods.get(7).getStereotype().isPresent());
		assertEquals(1, ods.get(7).getStereotype().get().getValues().size());
		assertEquals("someRandomStereotype", ods.get(7).getStereotype().get().getValues().get(0).getName());
	}

}