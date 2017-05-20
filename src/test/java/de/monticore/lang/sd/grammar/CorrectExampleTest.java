package de.monticore.lang.sd.grammar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.Test;

import de.monticore.lang.sd._ast.ASTSDCompilationUnit;
import de.monticore.lang.sd._parser.SDParser;
import de.se_rwth.commons.logging.Log;

public class CorrectExampleTest {

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
	public void testCorrectExample() throws IOException {
		// Load model
		ASTSDCompilationUnit sd = loadModel("src/test/resources/examples/correct/example.sd");

		// Traverse AST and check for correctness
		assertEquals(15, sd.getSequenceDiagram().getSDElements().size());
	}

}
