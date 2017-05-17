package de.monticore.lang.sd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.Test;

import de.monticore.lang.sd._ast.ASTSequenceDiagram;
import de.monticore.lang.sd._parser.SDParser;

public class CorrectExampleTest {

	private ASTSequenceDiagram loadModel(String modelName) throws IOException {
		// Load model
		Path model = Paths.get(modelName);
		SDParser parser = new SDParser();
		Optional<ASTSequenceDiagram> sd = parser.parseSequenceDiagram(model.toString());
		assertFalse(parser.hasErrors());
		assertTrue(sd.isPresent());
		return sd.get();
	}

	@Test
	public void testCorrectExample() throws IOException {
		// Load model
		ASTSequenceDiagram sd = loadModel("src/test/resources/examples/correct/example.sd");

		// Traverse AST and check for correctness
		assertEquals(15, sd.getSDElements().size());
	}

}
